package com.hglee.account.accounts.application.service;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.BDDAssertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.hglee.account.accounts.application.command.RequestAccountVerificationCommand;
import com.hglee.account.accounts.application.usecase.RequestAccountVerificationUseCase;
import com.hglee.account.accounts.domain.Account;
import com.hglee.account.accounts.domain.Status;
import com.hglee.account.accounts.domain.event.RequestedAccountVerificationEvent;
import com.hglee.account.accounts.domain.repository.IAccountRepository;
import com.hglee.account.accounts.factory.AccountFactory;
import com.hglee.account.core.IEventPublisher;

@SpringBootTest
class RequestAccountVerificationServiceTest {
	@Autowired
	IAccountRepository accountRepository;

	RequestAccountVerificationUseCase useCase;

	@Mock
	IEventPublisher eventPublisher = event -> {
	};

	@BeforeEach
	void before() {
		useCase = new RequestAccountVerificationService(accountRepository, eventPublisher);
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

				Account account = accountRepository.findByMobile(mobile).get();

				then(account.getStatus()).isEqualTo(Status.VERIFICATION_REQUESTED);
				then(account.isExpiredPinCode()).isFalse();
			}
		}

		@Nested
		@DisplayName("Context: 동일한 전화번호로 생성된 임시계정이 존재하는 경우")
		class context_not_exist_same_mobile_temporary_account {
			String mobile;
			Account existTemporaryAccount;

			@BeforeEach
			void before() {
				AccountFactory accountFactory = AccountFactory.build();

				mobile = accountFactory.getMobile();

				existTemporaryAccount = accountRepository.save(Account.ofRequestVerificationByMobile(mobile));
			}

			@Test
			@DisplayName("It: 새로운 코드를 생성하고 이벤트를 전파한다.")
			void it_should_create_account() {
				useCase.execute(new RequestAccountVerificationCommand(mobile));

				ArgumentCaptor<RequestedAccountVerificationEvent> argument = ArgumentCaptor.forClass(
						RequestedAccountVerificationEvent.class);

				verify(eventPublisher, times(1)).publish(argument.capture());

				Account account = accountRepository.findByMobile(mobile).get();

				then(account.getStatus()).isEqualTo(Status.VERIFICATION_REQUESTED);
				then(account.isExpiredPinCode()).isFalse();
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
			@DisplayName("It: IllegalArgumentException이 발생한다.")
			void throw_error() {
				assertThatThrownBy(() -> useCase.execute(new RequestAccountVerificationCommand(mobile))).isInstanceOf(
						IllegalArgumentException.class);
			}
		}
	}
}