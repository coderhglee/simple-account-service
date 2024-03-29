package com.hglee.account.auth.ui;

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
import com.hglee.account.accounts.application.in.usecase.SignUpWithMobileAndEmailUseCase;
import com.hglee.account.accounts.domain.Account;
import com.hglee.account.accounts.domain.repository.IAccountRepository;
import com.hglee.account.accounts.factory.AccountFactory;

class SignInControllerTest extends AcceptanceTest {
	@Autowired
	IAccountRepository accountRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	SignUpWithMobileAndEmailUseCase signUpWithMobileAndEmailUseCase;

	@Nested
	@DisplayName("Describe: POST /sign-in/mobile")
	class post_login {
		@Nested
		@DisplayName("Context: 회원가입된 사용자가 로그인을 요청 했을때 패스워드가 일치하는 경우")
		class exist_account_password_equals {
			AccountFactory accountFactory;

			@BeforeEach
			void before_each() {
				accountFactory = AccountFactory.build();

				회원가입_완료_됨(accountFactory.getMobile(), accountFactory.getEmail(), accountFactory.getPassword());
			}

			@DisplayName("It: Access Token을 발급 받을수 있다.")
			@Test
			void success_login() {
				Map<String, String> params = new HashMap<>();
				params.put("mobile", accountFactory.getMobile());
				params.put("password", accountFactory.getPassword());

				given().body(params)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.when()
						.post("/sign-in/mobile")
						.then()
						.apply(print())
						.assertThat(status().isOk())
						.body("accessToken", instanceOf(String.class));
			}
		}

		@Nested
		@DisplayName("Context: 회원가입된 사용자가 로그인을 요청 했을때 패스워드가 일치하지 않는 경우")
		class exist_account_password_not_equals {
			AccountFactory accountFactory;

			@BeforeEach
			void before_each() {
				accountFactory = AccountFactory.build();

				회원가입_완료_됨(accountFactory.getMobile(), accountFactory.getEmail(), accountFactory.getPassword());
			}

			@DisplayName("It: 403에러가 발생한다.")
			@Test
			void fail_login_403() {
				Map<String, String> params = new HashMap<>();
				params.put("mobile", accountFactory.getMobile());
				params.put("password", "some_password");

				given().body(params)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.when()
						.post("/sign-in/mobile")
						.then()
						.apply(print())
						.assertThat(status().isForbidden());
			}
		}

		@Nested
		@DisplayName("Context: 회원가입 되지 않은 사용자가 로그인을 요청한 경우")
		class not_exist_account {

			@DisplayName("It: 404에러가 발생한다.")
			@Test
			void fail_login_404() {
				AccountFactory accountFactory = AccountFactory.build();

				Map<String, String> params = new HashMap<>();
				params.put("mobile", accountFactory.getMobile());
				params.put("password", accountFactory.getPassword());

				given().body(params)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.when()
						.post("/sign-in/mobile")
						.then()
						.apply(print())
						.assertThat(status().isNotFound());
			}
		}
	}

	@Nested
	@DisplayName("Describe: POST /sign-in/email")
	class post_email_login {
		@Nested
		@DisplayName("Context: 회원가입된 사용자가 로그인을 요청 했을때 패스워드가 일치하는 경우")
		class exist_account_password_equals {
			AccountFactory accountFactory;

			@BeforeEach
			void before_each() {
				accountFactory = AccountFactory.build();

				회원가입_완료_됨(accountFactory.getMobile(), accountFactory.getEmail(), accountFactory.getPassword());
			}

			@DisplayName("It: Access Token을 발급 받을수 있다.")
			@Test
			void success_login() {
				Map<String, String> params = new HashMap<>();
				params.put("email", accountFactory.getEmail());
				params.put("password", accountFactory.getPassword());

				given().body(params)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.when()
						.post("/sign-in/email")
						.then()
						.apply(print())
						.assertThat(status().isOk())
						.body("accessToken", instanceOf(String.class));
			}
		}

		@Nested
		@DisplayName("Context: 회원가입된 사용자가 로그인을 요청 했을때 패스워드가 일치하지 않는 경우")
		class exist_account_password_not_equals {
			AccountFactory accountFactory;

			@BeforeEach
			void before_each() {
				accountFactory = AccountFactory.build();

				회원가입_완료_됨(accountFactory.getMobile(), accountFactory.getEmail(), accountFactory.getPassword());
			}

			@DisplayName("It: 403에러가 발생한다.")
			@Test
			void fail_login_403() {
				Map<String, String> params = new HashMap<>();
				params.put("email", accountFactory.getEmail());
				params.put("password", "some_password");

				given().body(params)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.when()
						.post("/sign-in/email")
						.then()
						.apply(print())
						.assertThat(status().isForbidden());
			}
		}

		@Nested
		@DisplayName("Context: 회원가입 되지 않은 사용자가 로그인을 요청한 경우")
		class not_exist_account {

			@DisplayName("It: 404에러가 발생한다.")
			@Test
			void fail_login_404() {
				AccountFactory accountFactory = AccountFactory.build();

				Map<String, String> params = new HashMap<>();
				params.put("email", accountFactory.getEmail());
				params.put("password", accountFactory.getPassword());

				given().body(params)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.when()
						.post("/sign-in/email")
						.then()
						.apply(print())
						.assertThat(status().isNotFound());
			}
		}
	}

	@Nested
	@DisplayName("Context: 회원가입된 사용자가 로그인을 요청 했을때 패스워드가 일치하는 경우")
	class exist_account_password_equals {
		AccountFactory accountFactory;

		@BeforeEach
		void before_each() {
			accountFactory = AccountFactory.build();

			회원가입_완료_됨(accountFactory.getMobile(), accountFactory.getEmail(), accountFactory.getPassword());
		}

		@DisplayName("It: 모바일, 이메일 로그인을 요청하여 Access Token을 발급 받을수 있다.")
		@Test
		void success_mobile_and_email_login() {
			Map<String, String> mobileLoginParams = new HashMap<>();
			mobileLoginParams.put("mobile", accountFactory.getMobile());
			mobileLoginParams.put("password", accountFactory.getPassword());

			given().body(mobileLoginParams)
					.contentType(MediaType.APPLICATION_JSON_VALUE)
					.when()
					.post("/sign-in/mobile")
					.then()
					.apply(print())
					.assertThat(status().isOk())
					.body("accessToken", instanceOf(String.class));

			Map<String, String> emailLoginParams = new HashMap<>();
			emailLoginParams.put("email", accountFactory.getEmail());
			emailLoginParams.put("password", accountFactory.getPassword());

			given().body(emailLoginParams)
					.contentType(MediaType.APPLICATION_JSON_VALUE)
					.when()
					.post("/sign-in/email")
					.then()
					.apply(print())
					.assertThat(status().isOk())
					.body("accessToken", instanceOf(String.class));
		}
	}

	private Account 회원가입_완료_됨(String mobile, String email, String password) {
		Account signedUpAccount = AccountFactory.isSignedUpAccount(mobile, email,passwordEncoder.encode(password));

		accountRepository.save(signedUpAccount);

		return signedUpAccount;
	}
}