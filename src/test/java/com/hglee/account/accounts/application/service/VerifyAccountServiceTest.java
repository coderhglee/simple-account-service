package com.hglee.account.accounts.application.service;

import static org.assertj.core.api.BDDAssertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.hglee.account.accounts.application.command.VerifyAccountCommand;
import com.hglee.account.accounts.application.usecase.VerifyAccountUseCase;
import com.hglee.account.accounts.domain.repository.IAccountRepository;
import com.hglee.account.accounts.factory.AccountFactory;
import com.hglee.account.accounts.factory.PinCodeFactory;
import com.hglee.account.verificationCode.application.usecase.VerifyVerificationCodeUseCase;
import com.hglee.account.verificationCode.domain.VerificationCode;
import com.hglee.account.verificationCode.domain.VerificationCodeId;
import com.hglee.account.verificationCode.domain.repository.IVerificationCodeRepository;

@SpringBootTest
class VerifyAccountServiceTest {
	@Autowired
	IAccountRepository accountRepository;

	@Autowired
	VerifyVerificationCodeUseCase verifyVerificationCodeUseCase;

	VerifyAccountUseCase useCase;

	@Autowired
	IVerificationCodeRepository verificationCodeRepository;

	@BeforeEach
	void before() {
		useCase = new VerifyAccountService(verifyVerificationCodeUseCase);
	}

	@Nested
	@DisplayName("Describe: VerifyAccountService")
	class Describe_verify_account_service {
		@Nested
		@DisplayName("Context: 모바일로 인증이 요청되지 않은 경우")
		class Context_requested_verification_by_mobile {
			String mobile;
			String code;

			@BeforeEach
			void before() {
				AccountFactory accountFactory = AccountFactory.build();

				mobile = accountFactory.getMobile();
				code = accountFactory.getPinCode().getCode();

				회원가입_인증코드_발급됨(mobile, code, LocalDateTime.now().plusMinutes(1));
			}

			@Test
			@DisplayName("계정이 인증된다.")
			void it_verified_account() {
				assertThatNoException().isThrownBy(() -> useCase.execute(new VerifyAccountCommand(mobile, code)));
			}
		}

		@Nested
		@DisplayName("Context: 모바일로 인증이 요청되지 않은 경우")
		class Context_not_requested_verification_by_mobile {
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
				then(catchThrowable(() -> useCase.execute(new VerifyAccountCommand(mobile, code)))).isInstanceOf(
						IllegalArgumentException.class).hasMessageContaining("해당 모바일로 요청된 인증코드가 존재하지 않습니다.");
			}
		}

		@Nested
		@DisplayName("Context: 요청한 인증코드와 일치하지 않는 경우")
		class Context_not_equal_pin_code {
			String mobile;

			@BeforeEach
			void before() {
				AccountFactory accountFactory = AccountFactory.build();
				PinCodeFactory pincodeFactory = PinCodeFactory.build();

				mobile = accountFactory.getMobile();
				String code = pincodeFactory.getCode();

				회원가입_인증코드_발급됨(mobile, code, LocalDateTime.now().plusMinutes(1));
			}

			@Test
			@DisplayName("인증코드가 일치하지 않는다는 에러가 발생한다.")
			void it_throw_error() {
				then(catchThrowable(() -> useCase.execute(
						new VerifyAccountCommand(mobile, PinCodeFactory.build().getCode())))).isInstanceOf(
						IllegalArgumentException.class).hasMessageContaining("해당 모바일로 요청된 인증코드가 존재하지 않습니다.");
			}
		}

		@Nested
		@DisplayName("Context: 요청한 인증코드가 만료된 경우")
		class Context_expires_pin_code {
			String mobile;
			String code;

			@BeforeEach
			void before() {
				AccountFactory accountFactory = AccountFactory.build();
				PinCodeFactory pincodeFactory = PinCodeFactory.build();

				mobile = accountFactory.getMobile();
				code = pincodeFactory.getCode();

				회원가입_인증코드_발급됨(mobile, code, LocalDateTime.now());
			}

			@DisplayName("인증코드가 만료되었다는 에러가 발생한다.")
			@Test
			void it_throw_error() {
				then(catchThrowable(() -> useCase.execute(new VerifyAccountCommand(mobile, code)))).isInstanceOf(
						IllegalArgumentException.class).hasMessageContaining("인증코드가 만료되었습니다.");
			}
		}
	}

	private void 회원가입_인증코드_발급됨(String mobile, String code, LocalDateTime expiresAt) {
		VerificationCode verificationCode = new VerificationCode(new VerificationCodeId(mobile, code), LocalDateTime.now(), expiresAt);

		verificationCodeRepository.save(verificationCode);
	}
}