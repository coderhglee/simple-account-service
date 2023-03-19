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

import com.hglee.account.AcceptanceTest;
import com.hglee.account.accounts.domain.Account;
import com.hglee.account.accounts.dto.RequestAccountVerificationMobileResponse;
import com.hglee.account.accounts.factory.AccountFactory;
import com.hglee.account.accounts.persistence.repository.AccountRepository;
import com.hglee.account.auth.application.InteractionProvider;
import com.hglee.account.verificationCode.domain.VerificationCode;
import com.hglee.account.verificationCode.domain.repository.IVerificationCodeRepository;

class SignUpControllerTest extends AcceptanceTest {

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	IVerificationCodeRepository verificationCodeRepository;

	@Autowired
	InteractionProvider interactionProvider;

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
						.assertThat(status().isConflict());
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
			String interactionId;

			@BeforeEach
			void before() {
				AccountFactory accountFactory = AccountFactory.build();
				mobile = accountFactory.getMobile();

				code = 인증코드_발급됨(mobile);
				interactionId = Interaction_생성됨();
			}

			@DisplayName("It: 코드를 인증할 수 있다.")
			@Test
			void success_request_verification() {
				Map<String, String> params = new HashMap<>();
				params.put("mobile", mobile);
				params.put("code", code);
				params.put("interactionId", interactionId);

				given().body(params)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.when()
						.post("/sign-up/verify-mobile")
						.then()
						.apply(print())
						.assertThat(status().isOk());
			}
		}

		@Nested
		@DisplayName("Context: 만료된 인증코드인 경우")
		class expires_pin_code {
			String mobile;
			String code;
			String interactionId;

			@BeforeEach
			void before() {
				AccountFactory accountFactory = AccountFactory.build();
				mobile = accountFactory.getMobile();

				code = 인증코드_인증됨(mobile);
				interactionId = Interaction_생성됨();
			}

			@DisplayName("It: 403 에러가 발생한다.")
			@Test
			void failed_request_verification() {
				Map<String, String> params = new HashMap<>();
				params.put("mobile", mobile);
				params.put("code", code);
				params.put("interactionId", interactionId);

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
			String code;
			String interactionId;

			@BeforeEach
			void before() {
				AccountFactory accountFactory = AccountFactory.build();

				mobile = accountFactory.getMobile();

				code = 인증코드_인증됨(mobile);
				interactionId = Interaction_생성됨();
			}

			@DisplayName("It: 403 에러가 발생한다.")
			@Test
			void failed_request_verification() {
				Map<String, String> params = new HashMap<>();
				params.put("mobile", mobile);
				params.put("code", code);
				params.put("interactionId", interactionId);

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
			String interactionId;

			@BeforeEach
			void before() {
				AccountFactory accountFactory = AccountFactory.build();

				mobile = accountFactory.getMobile();

				전화번호_인증_요청(mobile);
				interactionId = Interaction_생성됨();
			}

			@DisplayName("It: 403 에러가 발생한다.")
			@Test
			void failed_request_verification() {
				Map<String, String> params = new HashMap<>();
				params.put("mobile", mobile);
				params.put("code", "123456");
				params.put("interactionId", interactionId);

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
	}

	@Nested
	@DisplayName("Describe: POST /sign-up/mobile")
	class sign_up_mobile {
		@Nested
		@DisplayName("동일한 이메일로 가입된 계정이 없는 경우")
		class not_exist_email {
			String mobile;
			String code;
			String interactionId;
			AccountFactory factory;

			@BeforeEach
			void before() {
				factory = AccountFactory.build();
				mobile = factory.getMobile();

				code = 인증코드_인증됨(mobile);
				interactionId = Interaction_생성됨();
			}

			@DisplayName("회원가입 할 수 있다.")
			@Test
			void success_sign_up_mobile() {
				Map<String, String> params = new HashMap<>();
				params.put("mobile", mobile);
				params.put("code", code);
				params.put("password", factory.getPassword());
				params.put("email", factory.getEmail());
				params.put("name", factory.getName());
				params.put("nickName", factory.getNickName());
				params.put("interactionId", interactionId);

				given().body(params)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.when()
						.post("/sign-up/mobile")
						.then()
						.apply(print())
						.assertThat(status().isOk())
						.body("accessToken", instanceOf(String.class));
			}
		}

		@Nested
		@DisplayName("계정 입력 형식이 올바르지 않는 경우")
		class invalid_account_format {
			String interactionId;

			@BeforeEach
			void before() {
				interactionId = Interaction_생성됨();
			}

			@DisplayName("validation error가 발생한다.")
			@Test
			void success_sign_up_mobile() {
				Map<String, String> params = new HashMap<>();
				params.put("mobile", "010-1234-1234");
				params.put("password", "1");
				params.put("code", "123456");
				params.put("email", "email.com");
				params.put("name", "***!");
				params.put("nickName", "some*");
				params.put("interactionId", interactionId);

				given().body(params)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.when()
						.post("/sign-up/mobile")
						.then()
						.apply(print())
						.assertThat(status().isUnprocessableEntity())
						.body("errors", hasSize(5));
			}
		}

		@Nested
		@DisplayName("전화번호 인증이 완료되지 않은 경우")
		class not_verify_mobile {
			String mobile;
			String code;
			String interactionId;
			AccountFactory factory;

			@BeforeEach
			void before() {
				factory = AccountFactory.build();
				mobile = factory.getMobile();

				code = 인증코드_발급됨(mobile);
				interactionId = Interaction_생성됨();
			}

			@DisplayName("에러가 발생한다.")
			@Test
			void success_sign_up_mobile() {
				Map<String, String> params = new HashMap<>();
				params.put("mobile", mobile);
				params.put("code", code);
				params.put("password", factory.getPassword());
				params.put("email", factory.getEmail());
				params.put("name", factory.getName());
				params.put("nickName", factory.getNickName());
				params.put("interactionId", interactionId);

				given().body(params)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.when()
						.post("/sign-up/mobile")
						.then()
						.apply(print())
						.assertThat(status().is4xxClientError())
						.body("message", containsString("전화번호 인증이 완료되지 않았습니다."));
			}
		}

		@Nested
		@DisplayName("전화번호 인증이 요청되지 않은 경우")
		class not_request_verification_mobile {
			String mobile;
			String interactionId;
			AccountFactory factory;

			@BeforeEach
			void before() {
				factory = AccountFactory.build();
				mobile = factory.getMobile();
				interactionId = Interaction_생성됨();
			}

			@DisplayName("에러가 발생한다.")
			@Test
			void success_sign_up_mobile() {
				Map<String, String> params = new HashMap<>();
				params.put("mobile", mobile);
				params.put("code", "123456");
				params.put("password", factory.getPassword());
				params.put("email", factory.getEmail());
				params.put("name", factory.getName());
				params.put("nickName", factory.getNickName());
				params.put("interactionId", interactionId);

				given().body(params)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.when()
						.post("/sign-up/mobile")
						.then()
						.apply(print())
						.assertThat(status().is4xxClientError())
						.body("message", containsString("전화번호 인증이 완료되지 않았습니다."));
			}
		}

		@Nested
		@DisplayName("동일한 이메일로 가입된 계정이 있는 경우")
		class exist_email_account {
			String mobile;
			String email;

			String code;
			String interactionId;

			@BeforeEach
			void before() {
				Account signedUpAccount = AccountFactory.isSignedUpAccount();
				accountRepository.save(signedUpAccount);

				mobile = signedUpAccount.getMobile();
				email = signedUpAccount.getEmail();

				code = 인증코드_인증됨(mobile);
				interactionId = Interaction_생성됨();
			}

			@DisplayName("동일한 이메일로 가입된 계정 에러가 발생한다.")
			@Test
			void failed_sign_up() {
				AccountFactory factory = AccountFactory.build();
				Map<String, String> params = new HashMap<>();
				params.put("mobile", factory.getMobile());
				params.put("code", code);
				params.put("password", factory.getPassword());
				params.put("email", email);
				params.put("name", factory.getName());
				params.put("nickName", factory.getNickName());
				params.put("interactionId", interactionId);

				given().body(params)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.when()
						.post("/sign-up/mobile")
						.then()
						.apply(print())
						.assertThat(status().isConflict())
						.body("message", containsString("이미 동일한 이메일로 가입된 계정입니다."));
			}
		}

		@Nested
		@DisplayName("동일한 전화번호로 가입된 계정이 있는 경우")
		class exist_mobile_account {
			String mobile;
			String email;
			String interactionId;

			@BeforeEach
			void before() {
				Account signedUpAccount = AccountFactory.isSignedUpAccount();
				accountRepository.save(signedUpAccount);

				mobile = signedUpAccount.getMobile();
				email = signedUpAccount.getEmail();
				interactionId = Interaction_생성됨();
			}

			@DisplayName("동일한 전화번호로 가입된 계정 에러가 발생한다.")
			@Test
			void failed_sign_up() {
				AccountFactory factory = AccountFactory.build();
				Map<String, String> params = new HashMap<>();
				params.put("mobile", mobile);
				params.put("code", "123456");
				params.put("password", factory.getPassword());
				params.put("email", factory.getEmail());
				params.put("name", factory.getName());
				params.put("nickName", factory.getNickName());
				params.put("interactionId", interactionId);

				given().body(params)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.when()
						.post("/sign-up/mobile")
						.then()
						.apply(print())
						.assertThat(status().isConflict())
						.body("message", containsString("이미 동일한 전화번호로 가입된 계정입니다."));
			}
		}
	}

	private RequestAccountVerificationMobileResponse 전화번호_인증_요청(String mobile) {
		Map<String, String> params = new HashMap<>();
		params.put("mobile", mobile);

		return given().body(params)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when()
				.post("/sign-up/request-account-verification-mobile")
				.then()
				.extract()
				.as(RequestAccountVerificationMobileResponse.class);
	}

	private String 인증코드_발급됨(String mobile) {
		VerificationCode verificationCode = VerificationCode.generate(mobile);
		verificationCodeRepository.save(verificationCode);
		return verificationCode.getCode();
	}

	private String Interaction_생성됨() {
		return interactionProvider.create("sign-up").getInteractionId();
	}

	private String 인증코드_인증됨(String mobile) {
		VerificationCode verificationCode = VerificationCode.generate(mobile);
		verificationCode.verify();
		verificationCodeRepository.save(verificationCode);
		return verificationCode.getCode();
	}
}