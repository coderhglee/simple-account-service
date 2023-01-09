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

@SpringBootTest
class ResetPasswordServiceTest {

	@Autowired
	IAccountRepository accountRepository;

	@Autowired
	PasswordEncoder encoder;

	ResetPasswordUseCase useCase;

	@BeforeEach
	void before() {
		useCase = new ResetPasswordService(accountRepository, encoder);
	}

	@Nested
	@DisplayName("Describe: 패드워드 재설정을 요청하면")
	class reset_password_service {
		@Nested
		@DisplayName("Context: 인증 완료된 계정인 경우")
		class confirmed_password_account {
			Account signedUpAccount;
			String password;

			@BeforeEach
			void before() {
				AccountFactory factory = AccountFactory.build();
				password = factory.getPassword();
				signedUpAccount = AccountFactory.isSignedUpAccount(encoder.encode(password));

				signedUpAccount.requestPasswordReset();
				signedUpAccount.confirmPasswordReset();

				accountRepository.save(signedUpAccount);
			}

			@DisplayName("계정의 비밀번호가 변경된다.")
			@Test
			void reset_password() {
				String newPassword = "new_password";
				useCase.execute(new ResetPasswordCommand(signedUpAccount.getMobile(),
						signedUpAccount.getPasswordResetRequest().getCode(), newPassword, newPassword));

				Account account = accountRepository.findByMobile(signedUpAccount.getMobile()).get();

				then(encoder.matches(newPassword, account.getPassword())).isTrue();
			}
		}

		@Nested
		@DisplayName("Context: 비밀번호와 비밀번호 확인 문자가 일치하지 않으면")
		class not_equals_password_confirm {
			@DisplayName("에러가 발생한다.")
			@Test
			void throw_error() {
				Account signedUpAccount = AccountFactory.isSignedUpAccount();
				assertThatThrownBy(() -> useCase.execute(
						new ResetPasswordCommand(signedUpAccount.getMobile(), "123456", "new_password",
								"some_password"))).isInstanceOf(IllegalArgumentException.class)
						.hasMessageContaining("패스워드가 일치하지 않습니다.");
			}
		}

		@Nested
		@DisplayName("Context: 이전 패스워드와 일치하는 경우")
		class equals_old_password_account {
			Account signedUpAccount;
			String password;

			@BeforeEach
			void before() {
				AccountFactory factory = AccountFactory.build();
				password = factory.getPassword();
				signedUpAccount = AccountFactory.isSignedUpAccount(encoder.encode(password));

				signedUpAccount.requestPasswordReset();
				signedUpAccount.confirmPasswordReset();

				accountRepository.save(signedUpAccount);
			}

			@DisplayName("에러가 발생한다.")
			@Test
			void throw_error() {
				assertThatThrownBy(() -> useCase.execute(new ResetPasswordCommand(signedUpAccount.getMobile(),
						signedUpAccount.getPasswordResetRequest().getCode(), password, password))).isInstanceOf(
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

				signedUpAccount.requestPasswordReset();
				signedUpAccount.confirmPasswordReset();

				accountRepository.save(signedUpAccount);
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
}