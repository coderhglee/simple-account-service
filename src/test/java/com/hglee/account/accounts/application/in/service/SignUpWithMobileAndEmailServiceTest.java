package com.hglee.account.accounts.application.in.service;

import static org.assertj.core.api.BDDAssertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.hglee.account.accounts.application.in.command.SignUpWithMobileAndEmailCommand;
import com.hglee.account.accounts.application.in.service.SignUpWithMobileAndEmailService;
import com.hglee.account.accounts.application.in.usecase.SignUpWithMobileAndEmailUseCase;
import com.hglee.account.accounts.domain.Account;
import com.hglee.account.accounts.domain.Status;
import com.hglee.account.accounts.domain.repository.IAccountRepository;
import com.hglee.account.accounts.dto.AccountResponseDto;
import com.hglee.account.accounts.exception.ConflictException;
import com.hglee.account.accounts.factory.AccountFactory;
import com.hglee.account.verificationCode.application.usecase.FindVerificationCodeUseCase;
import com.hglee.account.verificationCode.domain.VerificationCode;
import com.hglee.account.verificationCode.domain.repository.IVerificationCodeRepository;

@SpringBootTest
class SignUpWithMobileAndEmailServiceTest {
	@Autowired
	IAccountRepository accountRepository;

	@Autowired
	IVerificationCodeRepository verificationCodeRepository;

	@Autowired
	FindVerificationCodeUseCase findVerificationCodeUseCase;

	@Autowired
	PasswordEncoder encoder;

	SignUpWithMobileAndEmailUseCase useCase;

	@BeforeEach
	void before() {
		useCase = new SignUpWithMobileAndEmailService(accountRepository, encoder, findVerificationCodeUseCase);
	}

	@Nested
	@DisplayName("Describe: SignUpWithMobileAndEmailService")
	class Describe_sign_up_with_mobile_and_email_service {
		@Nested
		@DisplayName("Context: 모바일 인증이 완료된 계정이 회원가입을 요청하는 경우")
		class Context_verified_mobile_account {
			@Nested
			@DisplayName("Context: 동일한 이메일이 존재하지 않으면")
			class not_exist_already_email {
				String mobile;
				String code;

				AccountFactory factory;

				@BeforeEach
				void before() {
					factory = AccountFactory.build();

					mobile = factory.getMobile();
					code = 회원가입_인증코드_인증됨(mobile);
				}

				@Test
				@DisplayName("회원가입을 할 수 있다.")
				void it_success_sign_up_with_mobile_and_email() {
					AccountResponseDto responseDto = useCase.execute(
							new SignUpWithMobileAndEmailCommand(mobile, factory.getEmail(), factory.getPassword(),
									factory.getName(), factory.getNickName(), code));

					then(responseDto.getEmail()).isEqualTo(factory.getEmail());
					then(responseDto.getMobile()).isEqualTo(mobile);
					then(responseDto.getName()).isEqualTo(factory.getName());
					then(responseDto.getNickName()).isEqualTo(factory.getNickName());
					then(responseDto.getStatus()).isEqualTo(Status.ACTIVATED.name());
				}
			}

			@Nested
			@DisplayName("Context: 동일한 이메일이 존재하면")
			class exist_already_email {
				String email;
				String mobile;
				String code;

				@BeforeEach
				void before() {
					Account signedUpAccount = AccountFactory.isSignedUpAccount();

					accountRepository.save(signedUpAccount);

					email = signedUpAccount.getEmail();

					AccountFactory factory = AccountFactory.build();
					mobile = factory.getMobile();
					code = 회원가입_인증코드_인증됨(mobile);
				}

				@Test
				@DisplayName("이미 동일한 이메일로 가입된 계정 오류가 발생한다.")
				void it_fail_sign_up_with_mobile_and_email() {
					AccountFactory factory = AccountFactory.build();

					then(catchThrowable(() -> useCase.execute(
							new SignUpWithMobileAndEmailCommand(mobile, email, factory.getPassword(), factory.getName(),
									factory.getNickName(), code)))).isInstanceOf(ConflictException.class)
							.hasMessageContaining("이미 동일한 이메일로 가입된 계정입니다.");

				}
			}

			@Nested
			@DisplayName("Context: 동일한 전화번호 존재하면")
			class exist_already_mobile {
				String mobile;
				String code;

				@BeforeEach
				void before() {
					Account signedUpAccount = AccountFactory.isSignedUpAccount();

					accountRepository.save(signedUpAccount);

					mobile = signedUpAccount.getMobile();

					code = 회원가입_인증코드_인증됨(mobile);

				}

				@Test
				@DisplayName("이미 동일한 전화번호로 가입된 계정 에러가 발생한다.")
				void it_fail_sign_up_with_mobile_and_email() {
					AccountFactory factory = AccountFactory.build();

					then(catchThrowable(() -> useCase.execute(
							new SignUpWithMobileAndEmailCommand(mobile, factory.getEmail(), factory.getPassword(),
									factory.getName(), factory.getNickName(), code)))).isInstanceOf(
							ConflictException.class).hasMessageContaining("이미 동일한 전화번호로 가입된 계정입니다.");

				}
			}

		}

		@Nested
		@DisplayName("Context: 모바일 인증되지 않은 계정이 회원가입을 요청하는 경우")
		class Context_not_verified_mobile_account {
			@Test
			@DisplayName("에러가 발생한다.")
			void it_fail_sign_up_with_mobile_and_email() {
				AccountFactory factory = AccountFactory.build();

				then(catchThrowable(() -> useCase.execute(
						new SignUpWithMobileAndEmailCommand(factory.getMobile(), factory.getEmail(),
								factory.getPassword(), factory.getName(), factory.getNickName(),
								"123456")))).isInstanceOf(IllegalArgumentException.class)
						.hasMessageContaining("전화번호 인증이 완료되지 않았습니다.");

			}
		}
	}

	private String 회원가입_인증코드_인증됨(String mobile) {
		VerificationCode verificationCode = VerificationCode.generate(mobile);
		verificationCode.verify();

		verificationCodeRepository.save((verificationCode));
		return verificationCode.getCode();
	}
}