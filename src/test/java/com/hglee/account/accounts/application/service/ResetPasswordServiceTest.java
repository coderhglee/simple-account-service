package com.hglee.account.accounts.application.service;

import static org.assertj.core.api.BDDAssertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.hglee.account.accounts.application.command.ResetPasswordCommand;
import com.hglee.account.accounts.application.usecase.ResetPasswordUseCase;
import com.hglee.account.accounts.domain.Account;
import com.hglee.account.accounts.domain.repository.IAccountRepository;
import com.hglee.account.accounts.factory.AccountFactory;
import com.hglee.account.verificationCode.application.usecase.FindVerificationCodeUseCase;
import com.hglee.account.verificationCode.domain.VerificationCode;
import com.hglee.account.verificationCode.domain.repository.IVerificationCodeRepository;

@SpringBootTest
class ResetPasswordServiceTest {

	@Autowired
	IAccountRepository accountRepository;

	@Autowired
	IVerificationCodeRepository verificationCodeRepository;

	@Autowired
	FindVerificationCodeUseCase findVerificationCodeUseCase;

	@Autowired
	PasswordEncoder encoder;

	ResetPasswordUseCase useCase;

	@BeforeEach
	void before() {
		useCase = new ResetPasswordService(accountRepository, findVerificationCodeUseCase, encoder);
	}

	@Nested
	@DisplayName("Describe: 패드워드 재설정을 요청하면")
	class reset_password_service {
		@Nested
		@DisplayName("Context: 인증 완료된 계정인 경우")
		class confirmed_password_account {
			Account signedUpAccount;
			String code;
			String password;

			@BeforeEach
			void before() {
				AccountFactory factory = AccountFactory.build();
				password = factory.getPassword();
				signedUpAccount = AccountFactory.isSignedUpAccount(encoder.encode(password));

				code = 비밀번호_변경_인증코드_인증됨(signedUpAccount.getMobile());

				accountRepository.save(signedUpAccount);
			}

			@DisplayName("계정의 비밀번호가 변경된다.")
			@Test
			void reset_password() {
				String newPassword = "new_password";
				useCase.execute(new ResetPasswordCommand(signedUpAccount.getMobile(), code, newPassword, newPassword));

				Account account = accountRepository.findByMobile(signedUpAccount.getMobile()).get();

				then(encoder.matches(newPassword, account.getPassword())).isTrue();
			}
		}

		@Nested
		@DisplayName("Context: 비밀번호와 비밀번호 확인 문자가 일치하지 않으면")
		class not_equals_password_confirm {
			String mobile;

			@BeforeEach
			void before() {
				Account signedUpAccount = AccountFactory.isSignedUpAccount();

				mobile = signedUpAccount.getMobile();

				비밀번호_변경_인증코드_발급됨(mobile);
			}

			@DisplayName("에러가 발생한다.")
			@Test
			void throw_error() {
				assertThatThrownBy(() -> useCase.execute(
						new ResetPasswordCommand(mobile, "123456", "new_password", "some_password"))).isInstanceOf(
						IllegalArgumentException.class).hasMessageContaining("패스워드가 일치하지 않습니다.");
			}
		}

		@Nested
		@DisplayName("Context: 이전 패스워드와 일치하는 경우")
		class equals_old_password_account {
			Account signedUpAccount;
			String password;
			String code;

			@BeforeEach
			void before() {
				AccountFactory factory = AccountFactory.build();
				password = factory.getPassword();
				signedUpAccount = AccountFactory.isSignedUpAccount(encoder.encode(password));

				accountRepository.save(signedUpAccount);

				code = 비밀번호_변경_인증코드_인증됨(signedUpAccount.getMobile());
			}

			@DisplayName("에러가 발생한다.")
			@Test
			void throw_error() {
				assertThatThrownBy(() -> useCase.execute(new ResetPasswordCommand(signedUpAccount.getMobile(),
						code, password, password))).isInstanceOf(
						IllegalArgumentException.class).hasMessageContaining("이전 패스워드와 일치합니다.");
			}
		}

		@Nested
		@DisplayName("Context: 인증코드가 일치하지 않는 경우")
		class not_equals_code {
			Account signedUpAccount;
			String password;

			@BeforeEach
			void before() {
				AccountFactory factory = AccountFactory.build();
				password = factory.getPassword();
				signedUpAccount = AccountFactory.isSignedUpAccount(encoder.encode(password));

				accountRepository.save(signedUpAccount);

				비밀번호_변경_인증코드_발급됨(signedUpAccount.getMobile());
			}

			@DisplayName("에러가 발생한다.")
			@Test
			void throw_error() {
				assertThatThrownBy(() -> useCase.execute(
						new ResetPasswordCommand(signedUpAccount.getMobile(), "123456", "new_password",
								"new_password"))).isInstanceOf(IllegalArgumentException.class)
						.hasMessageContaining("잘못된 인증코드 입니다.");
			}
		}
	}

	private String 비밀번호_변경_인증코드_발급됨(String mobile) {
		VerificationCode verificationCode = VerificationCode.generate(mobile);

		verificationCodeRepository.save((verificationCode));
		return verificationCode.getCode();
	}

	private String 비밀번호_변경_인증코드_인증됨(String mobile) {
		VerificationCode verificationCode = VerificationCode.generate(mobile);
		verificationCode.verify();

		verificationCodeRepository.save((verificationCode));
		return verificationCode.getCode();
	}
}