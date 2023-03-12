package com.hglee.account.accounts.application.service;

import static org.assertj.core.api.BDDAssertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.hglee.account.accounts.application.command.ConfirmPasswordResetCommand;
import com.hglee.account.accounts.application.usecase.ConfirmPasswordResetUseCase;
import com.hglee.account.accounts.domain.Account;
import com.hglee.account.accounts.domain.repository.IAccountRepository;
import com.hglee.account.accounts.exception.NotFoundException;
import com.hglee.account.accounts.factory.AccountFactory;
import com.hglee.account.verificationCode.application.usecase.VerifyVerificationCodeUseCase;
import com.hglee.account.verificationCode.domain.VerificationCode;
import com.hglee.account.verificationCode.domain.repository.IVerificationCodeRepository;

@SpringBootTest
class ConfirmPasswordResetServiceTest {
	@Autowired
	IAccountRepository accountRepository;

	@Autowired
	IVerificationCodeRepository verificationCodeRepository;

	@Autowired
	VerifyVerificationCodeUseCase verifyVerificationCodeUseCase;

	ConfirmPasswordResetUseCase useCase;

	@BeforeEach
	void before() {
		useCase = new ConfirmPasswordResetService(verifyVerificationCodeUseCase);
	}

	@Nested
	@DisplayName("Describe: ConfirmPasswordResetService")
	class Describe_confirm_password_reset_service {
		@Nested
		@DisplayName("Context: 비밀번호 변경 코드 요청이 되지 않은 경우")
		class Context_requested_password_by_mobile {
			@Test
			@DisplayName("에러가 발생한다.")
			void it_not_requested_password_reset_account() {
				assertThatThrownBy(
						() -> useCase.execute(new ConfirmPasswordResetCommand(AccountFactory.build().getMobile(),
								"123456"))).isInstanceOf(
						NotFoundException.class).hasMessageContaining("해당 모바일로 요청된 인증코드가 존재하지 않습니다.");
			}
		}

		@Nested
		@DisplayName("Context: 요청한 인증코드가 만료된 경우")
		class Context_expires_password_code {
			String mobile;
			String code;

			@BeforeEach
			void before() {
				Account signedUpAccount = AccountFactory.isSignedUpAccount();

				mobile = signedUpAccount.getMobile();

				code = 인증코드_인증됨(mobile);
			}

			@DisplayName("인증코드가 만료되었다는 에러가 발생한다.")
			@Test
			void it_throw_error() {
				then(catchThrowable(() -> useCase.execute(new ConfirmPasswordResetCommand(mobile, code)))).isInstanceOf(
						IllegalStateException.class).hasMessageContaining("인증코드가 만료되었습니다.");
			}
		}
	}

	private String 인증코드_인증됨(String mobile) {
		VerificationCode verificationCode = VerificationCode.generate(mobile);

		verificationCode.verify();
		verificationCodeRepository.save((verificationCode));

		return verificationCode.getCode();
	}

}