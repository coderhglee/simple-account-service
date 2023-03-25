package com.hglee.account.accounts.application.in.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.hglee.account.accounts.application.in.command.RequestAccountVerificationCommand;
import com.hglee.account.accounts.application.in.service.RequestAccountVerificationService;
import com.hglee.account.accounts.application.in.usecase.RequestAccountVerificationUseCase;
import com.hglee.account.accounts.domain.Account;
import com.hglee.account.accounts.domain.event.RequestedAccountVerificationEvent;
import com.hglee.account.accounts.domain.repository.IAccountRepository;
import com.hglee.account.accounts.exception.ConflictException;
import com.hglee.account.accounts.factory.AccountFactory;
import com.hglee.account.core.IEventPublisher;
import com.hglee.account.verificationCode.application.service.CreateVerificationCodeService;
import com.hglee.account.verificationCode.application.usecase.CreateVerificationCodeUseCase;
import com.hglee.account.verificationCode.domain.repository.IVerificationCodeRepository;

@SpringBootTest
class RequestAccountVerificationServiceTest {
	@Autowired
	IAccountRepository accountRepository;

	@Autowired
	IVerificationCodeRepository verificationCodeRepository;

	RequestAccountVerificationUseCase useCase;

	CreateVerificationCodeUseCase createVerificationCodeUseCase;

	@Mock
	IEventPublisher eventPublisher = event -> {
	};

	@BeforeEach
	void before() {
		createVerificationCodeUseCase = new CreateVerificationCodeService(verificationCodeRepository);
		useCase = new RequestAccountVerificationService(accountRepository, eventPublisher,
				createVerificationCodeUseCase);
	}

	@Nested
	@DisplayName("Describe: RequestAccountVerificationService")
	class Describe_request_account_verification_service {
		@Nested
		@DisplayName("Context: 동일한 전화번호로 가입된 계정이 존재하지 않는 경우")
		class context_not_exist_same_mobile {
			String mobile;

			@BeforeEach
			void before() {
				AccountFactory accountFactory = AccountFactory.build();

				mobile = accountFactory.getMobile();
			}

			@Test
			@DisplayName("It: 임시계정을 생성하고 이벤트를 전파한다.")
			void it_should_create_account() {
				useCase.execute(new RequestAccountVerificationCommand(mobile));

				ArgumentCaptor<RequestedAccountVerificationEvent> argument = ArgumentCaptor.forClass(
						RequestedAccountVerificationEvent.class);

				verify(eventPublisher, times(1)).publish(argument.capture());
			}
		}

		@Nested
		@DisplayName("Context: 동일한 전화번호로 생성된 임시계정이 존재하는 경우")
		class context_not_exist_same_mobile_temporary_account {
			String mobile;

			@BeforeEach
			void before() {
				mobile = AccountFactory.build().getMobile();
				useCase.execute(new RequestAccountVerificationCommand(mobile));
			}

			@Test
			@DisplayName("It: 새로운 코드를 생성하고 이벤트를 전파한다.")
			void it_should_create_account() {
				useCase.execute(new RequestAccountVerificationCommand(mobile));

				ArgumentCaptor<RequestedAccountVerificationEvent> argument = ArgumentCaptor.forClass(
						RequestedAccountVerificationEvent.class);

				verify(eventPublisher, times(2)).publish(argument.capture());
			}
		}

		@Nested
		@DisplayName("Context: 동일한 전화번호로 가입된 계정이 존재하는 경우")
		class context_exist_same_mobile {
			String mobile;

			@BeforeEach
			void before() {
				Account signedUpAccount = AccountFactory.isSignedUpAccount();

				mobile = signedUpAccount.getMobile();

				accountRepository.save(signedUpAccount);
			}

			@Test
			@DisplayName("It: ConflictException 발생한다.")
			void throw_error() {
				assertThatThrownBy(() -> useCase.execute(new RequestAccountVerificationCommand(mobile))).isInstanceOf(
						ConflictException.class);
			}
		}
	}
}