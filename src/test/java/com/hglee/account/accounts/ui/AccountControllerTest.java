package com.hglee.account.accounts.ui;

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
import com.hglee.account.accounts.domain.Account;
import com.hglee.account.accounts.domain.PinCode;
import com.hglee.account.accounts.domain.Status;
import com.hglee.account.accounts.dto.AccountResponseDto;
import com.hglee.account.accounts.factory.AccountFactory;
import com.hglee.account.accounts.persistence.repository.AccountRepository;
import com.hglee.account.auth.dto.AuthenticationResponse;

class AccountControllerTest extends AcceptanceTest {
	@Autowired
	AccountRepository accountRepository;

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

			@BeforeEach
			void before_each() {
				AccountFactory accountFactory = AccountFactory.build();
				accountResponse = 회원가입_요청(accountFactory);

				authResponse = 토큰_발급_요청(accountResponse.getMobile(), accountFactory.getPassword());
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
						.body("id", equalTo(accountResponse.getId()))
						.body("status", equalTo(accountResponse.getStatus()))
						.body("mobile", equalTo(accountResponse.getMobile()))
						.body("email", equalTo(accountResponse.getEmail()))
						.body("nickName", equalTo(accountResponse.getNickName()))
						.body("name", equalTo(accountResponse.getName()));
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

	private AccountResponseDto 회원가입_요청(AccountFactory factory) {
		Account verifiedAccount = new Account(factory.getMobile(), Status.VERIFIED, PinCode.generateCode());

		accountRepository.save(verifiedAccount);

		Map<String, String> params = new HashMap<>();
		params.put("mobile", factory.getMobile());
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
				.body("id", instanceOf(String.class))
				.body("mobile", equalTo(factory.getMobile()))
				.body("email", equalTo(factory.getEmail()))
				.body("name", equalTo(factory.getName()))
				.body("nickName", equalTo(factory.getNickName()))
				.body("status", equalTo(Status.ACTIVATED.name()))
				.extract()
				.as(AccountResponseDto.class);
	}
}