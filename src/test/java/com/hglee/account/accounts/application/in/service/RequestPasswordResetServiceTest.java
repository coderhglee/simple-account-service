package com.hglee.account.accounts.application.in.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.hglee.account.accounts.application.in.command.RequestPasswordResetCommand;
import com.hglee.account.accounts.application.in.usecase.RequestPasswordResetUseCase;
import com.hglee.account.accounts.domain.Account;
import com.hglee.account.accounts.domain.event.RequestedAccountVerificationEvent;
import com.hglee.account.accounts.domain.repository.IAccountRepository;
import com.hglee.account.accounts.exception.NotFoundException;
import com.hglee.account.accounts.factory.AccountFactory;
import com.hglee.account.accounts.infrastructure.client.IVerificationCodeClient;
import com.hglee.account.core.IEventPublisher;

@SpringBootTest
class RequestPasswordResetServiceTest {
	@Autowired
	IAccountRepository accountRepository;

	@Autowired
	IVerificationCodeClient verificationCodeClient;

	RequestPasswordResetUseCase useCase;

	@Mock
	IEventPublisher eventPublisher = event -> {
	};

	@BeforeEach
	void before() {
		useCase = new RequestPasswordResetService(accountRepository, eventPublisher, verificationCodeClient);
	}

	@Nested
	@DisplayName("Describe: RequestAccountVerificationService")
	class Describe_request_account_verification_service {
		@Nested
		@DisplayName("Context: 동일한 전화번호로 가입된 계정이 존재하는 경우")
		class context_not_exist_same_mobile {
			String mobile;

			@BeforeEach
			void before() {
				Account signedUpAccount = AccountFactory.isSignedUpAccount();

				mobile = signedUpAccount.getMobile();

				accountRepository.save(signedUpAccount);
			}

			@Test
			@DisplayName("It: 비밀번호 재설정 코드를 생성한다.")
			void it_should_create_request_reset_password() {
				useCase.execute(new RequestPasswordResetCommand(mobile));

				ArgumentCaptor<RequestedAccountVerificationEvent> argument = ArgumentCaptor.forClass(
						RequestedAccountVerificationEvent.class);

				verify(eventPublisher, times(1)).publish(argument.capture());
			}
		}

		@Nested
		@DisplayName("Context: 동일한 전화번호로 가입된 계정이 존재하지 않는 경우")
		class context_not_exist_same_mobile_account {
			String mobile;

			@BeforeEach
			void before() {
				AccountFactory accountFactory = AccountFactory.build();

				mobile = accountFactory.getMobile();
			}

			@Test
			@DisplayName("It: 에러가 발생한다.")
			void throw_error() {
				assertThatThrownBy(() -> useCase.execute(new RequestPasswordResetCommand(mobile))).isInstanceOf(
						NotFoundException.class);
			}
		}
	}
}