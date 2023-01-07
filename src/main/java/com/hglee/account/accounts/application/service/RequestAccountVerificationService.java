package com.hglee.account.accounts.application.service;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.hglee.account.accounts.application.command.RequestAccountVerificationCommand;
import com.hglee.account.accounts.application.usecase.RequestAccountVerificationUseCase;
import com.hglee.account.accounts.domain.Account;
import com.hglee.account.accounts.domain.event.RequestedAccountVerificationEvent;
import com.hglee.account.accounts.domain.repository.IAccountRepository;
import com.hglee.account.core.IEventPublisher;

@Service
public class RequestAccountVerificationService implements RequestAccountVerificationUseCase {
	private final IAccountRepository accountRepository;
	private final IEventPublisher eventPublisher;

	public RequestAccountVerificationService(IAccountRepository accountRepository, IEventPublisher eventPublisher) {
		this.accountRepository = accountRepository;
		this.eventPublisher = eventPublisher;
	}

	@Override
	@Transactional
	public void execute(RequestAccountVerificationCommand command) {
		String mobile = command.getMobile();

		Account account = accountRepository.findByMobile(mobile)
				.orElse(Account.ofRequestVerificationByMobile(command.getMobile()));

		if (account.isSignedUp()) {
			throw new IllegalArgumentException("이미 가입된 계정입니다.");
		}

		account.requestVerificationByMobile();

		Account createdAccount = this.accountRepository.save(account);

		this.eventPublisher.publish(new RequestedAccountVerificationEvent(createdAccount, createdAccount.getPinCode()));
	}
}
