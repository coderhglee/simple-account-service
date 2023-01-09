package com.hglee.account.accounts.application.service;

import static org.assertj.core.api.BDDAssertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.hglee.account.accounts.application.command.ConfirmPasswordResetCommand;
import com.hglee.account.accounts.application.usecase.ConfirmPasswordResetUseCase;
import com.hglee.account.accounts.domain.Account;
import com.hglee.account.accounts.domain.PasswordResetRequest;
import com.hglee.account.accounts.domain.Status;
import com.hglee.account.accounts.domain.repository.IAccountRepository;
import com.hglee.account.accounts.exception.NotFoundException;
import com.hglee.account.accounts.factory.AccountFactory;

@SpringBootTest
class ConfirmPasswordResetServiceTest {
	@Autowired
	IAccountRepository accountRepository;

	ConfirmPasswordResetUseCase useCase;

	@BeforeEach
	void before() {
		useCase = new ConfirmPasswordResetService(accountRepository);
	}

	@Nested
	@DisplayName("Describe: ConfirmPasswordResetService")
	class Describe_confirm_password_reset_service {
		@Nested
		@DisplayName("Context: 비밀번호 변경 코드 요청이 되지 않은 경우")
		class Context_requested_password_by_mobile {
			String mobile;

			@BeforeEach
			void before() {
				Account signedUpAccount = AccountFactory.isSignedUpAccount();

				mobile = signedUpAccount.getMobile();
				accountRepository.save(signedUpAccount);
			}

			@Test
			@DisplayName("에러가 발생한다.")
			void it_not_requested_password_reset_account() {
				assertThatThrownBy(
						() -> useCase.execute(new ConfirmPasswordResetCommand(mobile, "123456"))).isInstanceOf(
						IllegalArgumentException.class).hasMessageContaining("요청된 인증코드가 없습니다.");
			}
		}

		@Nested
		@DisplayName("Context: 모바일로 인증이 요청되지 않은 경우")
		class Context_not_requested_password_by_mobile {
			String mobile;
			String code;

			@BeforeEach
			void before() {
				AccountFactory accountFactory = AccountFactory.build();

				mobile = accountFactory.getMobile();
				code = accountFactory.getPinCode().getCode();
			}

			@Test
			@DisplayName("인증코드가 존재하지 않는다는 에러가 발생한다.")
			void it_throw_error() {
				then(catchThrowable(() -> useCase.execute(new ConfirmPasswordResetCommand(mobile, code)))).isInstanceOf(
						NotFoundException.class).hasMessageContaining("계정이 존재하지 않습니다.");
			}
		}

		@Nested
		@DisplayName("Context: 요청한 인증코드와 일치하지 않는 경우")
		class Context_not_equal_password_code {
			String mobile;

			@BeforeEach
			void before() {
				Account signedUpAccount = AccountFactory.isSignedUpAccount();

				signedUpAccount.requestPasswordReset();

				mobile = signedUpAccount.getMobile();

				accountRepository.save(signedUpAccount);
			}

			@Test
			@DisplayName("인증코드가 일치하지 않는다는 에러가 발생한다.")
			void it_throw_error() {
				then(catchThrowable(
						() -> useCase.execute(new ConfirmPasswordResetCommand(mobile, "123456")))).isInstanceOf(
						IllegalArgumentException.class).hasMessageContaining("인증코드가 일치하지 않습니다.");
			}
		}

		@Nested
		@DisplayName("Context: 요청한 인증코드가 만료된 경우")
		class Context_expires_password_code {
			String mobile;
			String code;

			@BeforeEach
			void before() {
				AccountFactory accountFactory = AccountFactory.build();

				mobile = accountFactory.getMobile();
				code = "123456";

				Account account = new Account(accountFactory.getId(), mobile, Status.ACTIVATED,
						new PasswordResetRequest(code, false, LocalDateTime.now(),
								LocalDateTime.now().minusMinutes(1L)));

				accountRepository.save(account);
			}

			@DisplayName("인증코드가 만료되었다는 에러가 발생한다.")
			@Test
			void it_throw_error() {
				then(catchThrowable(() -> useCase.execute(new ConfirmPasswordResetCommand(mobile, code)))).isInstanceOf(
						IllegalArgumentException.class).hasMessageContaining("인증코드가 만료되었습니다.");
			}
		}
	}
}