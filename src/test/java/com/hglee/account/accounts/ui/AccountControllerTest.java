package com.hglee.account.accounts.ui;

import static org.assertj.core.api.BDDAssertions.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.hglee.account.AcceptanceTest;
import com.hglee.account.accounts.domain.Status;
import com.hglee.account.accounts.domain.repository.IAccountRepository;
import com.hglee.account.accounts.dto.AccountResponseDto;
import com.hglee.account.accounts.factory.AccountFactory;
import com.hglee.account.auth.dto.AuthenticationResponse;
import com.hglee.account.verificationCode.domain.VerificationCode;
import com.hglee.account.verificationCode.domain.repository.IVerificationCodeRepository;

class AccountControllerTest extends AcceptanceTest {
	@Autowired
	IAccountRepository accountRepository;

	@Autowired
	IVerificationCodeRepository verificationCodeRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Nested
	@DisplayName("Describe: GET /me")
	class get_me {
		@Nested
		@DisplayName("Context: 토큰 발급 완료된 사용자가 내정보를 요청한 경우")
		class token_valid {
			AuthenticationResponse authResponse;

			AccountResponseDto accountResponse;

			AccountFactory accountFactory;

			@BeforeEach
			void before_each() {
				accountFactory = AccountFactory.build();
				authResponse = 회원가입_요청(accountFactory);
			}

			@DisplayName("It: 내정보를 조회 할 수 있다.")
			@Test
			void success_get_me() {
				given().header("Authorization", "Bearer " + authResponse.getAccessToken())
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.when()
						.get("/me")
						.then()
						.apply(print())
						.assertThat(status().isOk())
						.body("id", any(String.class))
						.body("status", equalTo(Status.ACTIVATED.name()))
						.body("mobile", equalTo(accountFactory.getMobile()))
						.body("email", equalTo(accountFactory.getEmail()))
						.body("nickName", equalTo(accountFactory.getNickName()))
						.body("name", equalTo(accountFactory.getName()));
			}
		}

		@Nested
		@DisplayName("Context: 토큰이 잘못된 경우")
		class token_invalid {
			@DisplayName("It: 401 에러가 발생한다.")
			@Test
			void fail_get_me() {
				given().header("Authorization", "Bearer some")
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.when()
						.get("/me")
						.then()
						.apply(print())
						.assertThat(status().isUnauthorized());
			}
		}
	}

	@Nested
	@DisplayName("Describe: POST /request-password-reset")
	class request_password_reset {
		@Nested
		@DisplayName("Context: 사용자가 패스워드 재설정 인증을 요청하면")
		class request_password_reset_mobile {
			String mobile;

			@BeforeEach
			void before_each() {
				AccountFactory accountFactory = AccountFactory.build();
				mobile = accountFactory.getMobile();
				회원가입_요청(accountFactory);
			}

			@DisplayName("It: 패스워드 인증 코드를 생성한다.")
			@Test
			void generate_password_reset_code() {
				패스워드_인증코드_생성_요청(mobile);
			}

		}
	}

	@Nested
	@DisplayName("Describe: POST /confirm-password-reset")
	class confirm_password_reset {
		@Nested
		@DisplayName("Context: 사용자가 패스워드 재설정 인증코드 확인을 요청하면")
		class request_password_reset_mobile {

			String mobile;
			String code;

			@BeforeEach
			void before_each() {
				AccountFactory accountFactory = AccountFactory.build();
				회원가입_요청(accountFactory);

				mobile = accountFactory.getMobile();

				code = 인증코드_발급됨(mobile);
			}

			@DisplayName("It: 패스워드 인증 코드를 인증할 수 있다.")
			@Test
			void success_get_me() {
				패스워드_인증코드_인증_요청(mobile, code);
			}

		}
	}

	@Nested
	@DisplayName("Describe: PATCH /password")
	class password_Reset {
		@Nested
		@DisplayName("Context: 사용자가 패스워드 재설정을 요청하면")
		class request_password_reset_mobile {
			String mobile;

			String code;

			@BeforeEach
			void before_each() {
				AccountFactory accountFactory = AccountFactory.build();
				mobile = accountFactory.getMobile();
				회원가입_요청(accountFactory);

				code = 인증코드_인증됨(mobile);
			}

			@DisplayName("It: 패스워드를 변경할 수 있다.")
			@Test
			void success_get_me() {
				Map<String, String> params = new HashMap<>();
				params.put("mobile", mobile);
				params.put("code", code);
				params.put("password", "some_password");
				params.put("passwordConfirm", "some_password");

				given().body(params)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.when()
						.patch("/password")
						.then()
						.apply(print())
						.assertThat(status().isNoContent());

				AuthenticationResponse authenticationResponse = 모바일_로그인_요청(mobile, "some_password");

				then(authenticationResponse.getAccessToken()).isInstanceOf(String.class);
			}
		}
	}

	private void 패스워드_인증코드_인증_요청(String mobile, String code) {
		Map<String, String> params = new HashMap<>();
		params.put("mobile", mobile);
		params.put("code", code);

		given().body(params)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when()
				.post("/confirm-password-reset")
				.then()
				.apply(print())
				.assertThat(status().isNoContent());
	}

	private AuthenticationResponse 토큰_발급_요청(String mobile, String password) {
		Map<String, String> loginParams = new HashMap<>();
		loginParams.put("mobile", mobile);
		loginParams.put("password", password);

		return given().body(loginParams)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when()
				.post("/sign-in/mobile")
				.then()
				.apply(print())
				.assertThat(status().isOk())
				.extract()
				.body()
				.as(AuthenticationResponse.class);
	}

	private AuthenticationResponse 회원가입_요청(AccountFactory factory) {
		String code = 인증코드_인증됨(factory.getMobile());

		Map<String, String> params = new HashMap<>();
		params.put("mobile", factory.getMobile());
		params.put("code", code);
		params.put("password", factory.getPassword());
		params.put("email", factory.getEmail());
		params.put("name", factory.getName());
		params.put("nickName", factory.getNickName());

		return given().body(params)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when()
				.post("/sign-up/mobile")
				.then()
				.apply(print())
				.assertThat(status().isOk())
				.extract()
				.body()
				.as(AuthenticationResponse.class);
	}

	private void 패스워드_인증코드_생성_요청(String mobile) {
		Map<String, String> params = new HashMap<>();
		params.put("mobile", mobile);

		given().body(params)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when()
				.post("/request-password-reset")
				.then()
				.apply(print())
				.assertThat(status().isNoContent());
	}

	private AuthenticationResponse 모바일_로그인_요청(String mobile, String password) {
		Map<String, String> params = new HashMap<>();
		params.put("mobile", mobile);
		params.put("password", password);

		return given().body(params)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when()
				.post("/sign-in/mobile")
				.then()
				.apply(print())
				.assertThat(status().isOk())
				.extract()
				.as(AuthenticationResponse.class);
	}

	private String 인증코드_발급됨(String mobile) {
		VerificationCode verificationCode = VerificationCode.generate(mobile);
		verificationCodeRepository.save(verificationCode);
		return verificationCode.getCode();
	}

	private String 인증코드_인증됨(String mobile) {
		VerificationCode verificationCode = VerificationCode.generate(mobile);
		verificationCode.verify();
		verificationCodeRepository.save(verificationCode);
		return verificationCode.getCode();
	}
}