package com.hglee.account.accounts.ui;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import com.hglee.account.AcceptanceTest;
import com.hglee.account.accounts.domain.Account;
import com.hglee.account.accounts.domain.PinCode;
import com.hglee.account.accounts.domain.Status;
import com.hglee.account.accounts.factory.AccountFactory;
import com.hglee.account.accounts.persistence.repository.AccountRepository;

class SignUpControllerTest extends AcceptanceTest {

	@Autowired
	AccountRepository accountRepository;

	@Nested
	@DisplayName("Describe: POST /sign-up/request-verification-account-mobile")
	class post_request_verification_account_mobile {
		@Nested
		@DisplayName("Context: 회원가입 하지 않은 전화번호인 경우")
		class not_exist_account_by_mobile {
			@Nested
			@DisplayName("Context: 올바른 전화번호를 입력한 경우")
			class valid_mobile_number {
				@DisplayName("It: 전화번호 인증을 요청할 수 있다.")
				@Test
				void success_request_verification() {
					AccountFactory accountFactory = AccountFactory.build();

					전화번호_인증_요청(accountFactory.getMobile());
				}
			}

			@Nested
			@DisplayName("Context: 동일한 전화번호로 여러번 요청하는 경우")
			class request_multiple_verification_mobile_number {
				@DisplayName("It: 새로운 인증번호가 발급된다.")
				@Test
				void success_request_verification() {
					AccountFactory accountFactory = AccountFactory.build();

					전화번호_인증_요청(accountFactory.getMobile());
					전화번호_인증_요청(accountFactory.getMobile());
				}
			}

			@Nested
			@DisplayName("Context: 잘못된 형식의 전화번호를 입력한 경우")
			class invalid_mobile_number {
				@DisplayName("It: 전화번호 인증을 요청할 수 있다.")
				@Test
				void failed_request_verification() {
					Map<String, String> params = new HashMap<>();
					params.put("mobile", "010-1234-1234");

					given().body(params)
							.contentType(MediaType.APPLICATION_JSON_VALUE)
							.when()
							.post("/sign-up/request-account-verification-mobile")
							.then()
							.apply(print())
							.assertThat(status().isUnprocessableEntity())
							.body("errors", hasSize(1));
				}
			}
		}

		@Nested
		@DisplayName("Context: 회원가입된 계정의 전화번호인 경우")
		class exist_account_by_mobile {
			String mobile;

			@BeforeEach
			void before() {
				Account signedUpAccount = AccountFactory.isSignedUpAccount();
				accountRepository.save(signedUpAccount);

				mobile = signedUpAccount.getMobile();
			}

			@DisplayName("It: 이미 가입된 계정이라는 에러가 발생한다.")
			@Test
			void failed_request_verification() {
				Map<String, String> params = new HashMap<>();
				params.put("mobile", mobile);

				given().body(params)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.when()
						.post("/sign-up/request-account-verification-mobile")
						.then()
						.apply(print())
						.assertThat(status().is4xxClientError());
			}
		}
	}

	@Nested
	@DisplayName("Describe: POST /sign-up/verify-mobile")
	class post_verify_mobile {
		@Nested
		@DisplayName("Context: 인증코드가 발급되고 유효한 인증코드인 경우")
		class not_expires_pin_code {
			String mobile;
			String code;

			@BeforeEach
			void before() {
				AccountFactory accountFactory = AccountFactory.build();

				mobile = accountFactory.getMobile();

				전화번호_인증_요청(mobile);
				Account account = accountRepository.findByMobile(mobile).get();
				code = account.getPinCode().getCode();
			}

			@DisplayName("It: 코드를 인증할 수 있다.")
			@Test
			void success_request_verification() {
				인증코드_인증_요청(mobile, code);
			}
		}

		@Nested
		@DisplayName("Context: 만료된 인증코드인 경우")
		class expires_pin_code {
			String mobile;
			String code;

			@BeforeEach
			void before() {
				AccountFactory accountFactory = AccountFactory.build();
				code = accountFactory.getPinCode().getCode();
				mobile = accountFactory.getMobile();

				Account account = new Account(accountFactory.getId(), mobile, accountFactory.getEmail(),
						Status.VERIFICATION_REQUESTED,
						accountFactory.getName(), accountFactory.getNickName(),
						new PinCode(code, LocalDateTime.now().minusMinutes(1), LocalDateTime.now()));

				accountRepository.save(account);
			}

			@DisplayName("It: 403 에러가 발생한다.")
			@Test
			void failed_request_verification() {
				Map<String, String> params = new HashMap<>();
				params.put("mobile", mobile);
				params.put("code", code);

				given().body(params)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.when()
						.post("/sign-up/verify-mobile")
						.then()
						.apply(print())
						.assertThat(status().is4xxClientError());
			}
		}

		@Nested
		@DisplayName("Context: 사용된 인증코드인 경우")
		class used_pin_code {
			String mobile;

			@BeforeEach
			void before() {
				AccountFactory accountFactory = AccountFactory.build();

				mobile = accountFactory.getMobile();

				전화번호_인증_요청(mobile);
			}

			@DisplayName("It: 403 에러가 발생한다.")
			@Test
			void failed_request_verification() {
				Map<String, String> params = new HashMap<>();
				params.put("mobile", mobile);
				params.put("code", "123456");

				given().body(params)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.when()
						.post("/sign-up/verify-mobile")
						.then()
						.apply(print())
						.assertThat(status().is4xxClientError());
			}
		}

		@Nested
		@DisplayName("Context: 일치하지 않는 인증코드인 경우")
		class unmatched_pin_code {
			String mobile;

			@BeforeEach
			void before() {
				AccountFactory accountFactory = AccountFactory.build();

				mobile = accountFactory.getMobile();

				전화번호_인증_요청(mobile);
			}

			@DisplayName("It: 403 에러가 발생한다.")
			@Test
			void failed_request_verification() {
				Map<String, String> params = new HashMap<>();
				params.put("mobile", mobile);
				params.put("code", "123456");

				given().body(params)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.when()
						.post("/sign-up/verify-mobile")
						.then()
						.apply(print())
						.assertThat(status().is4xxClientError());
			}
		}

		@Nested
		@DisplayName("Context: 이미 가입된 전화번호의 인증코드인 경우")
		class already_sign_up_account {
			String mobile;
			String code;

			@BeforeEach
			void before() {
				Account signedUpAccount = AccountFactory.isSignedUpAccount();

				mobile = signedUpAccount.getMobile();
				code = signedUpAccount.getPinCode().getCode();
			}

			@DisplayName("It: 403 에러가 발생한다.")
			@Test
			void failed_request_verification() {
				Map<String, String> params = new HashMap<>();
				params.put("mobile", mobile);
				params.put("code", code);

				given().body(params)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.when()
						.post("/sign-up/verify-mobile")
						.then()
						.apply(print())
						.assertThat(status().is4xxClientError());
			}
		}
	}

	private void 인증코드_인증_요청(String mobile, String code) {
		Map<String, String> params = new HashMap<>();
		params.put("mobile", mobile);
		params.put("code", code);

		given().body(params)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when()
				.post("/sign-up/verify-mobile")
				.then()
				.apply(print())
				.assertThat(status().isOk());
	}

	private void 전화번호_인증_요청(String mobile) {
		Map<String, String> params = new HashMap<>();
		params.put("mobile", mobile);

		given().body(params)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when()
				.post("/sign-up/request-account-verification-mobile")
				.then()
				.apply(print())
				.assertThat(status().isOk());
	}
}