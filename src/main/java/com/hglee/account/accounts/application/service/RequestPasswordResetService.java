package com.hglee.account.accounts.application.service;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.hglee.account.accounts.application.command.RequestPasswordResetCommand;
import com.hglee.account.accounts.application.usecase.RequestPasswordResetUseCase;
import com.hglee.account.accounts.domain.Account;
import com.hglee.account.accounts.domain.event.RequestedPasswordResetEvent;
import com.hglee.account.accounts.domain.repository.IAccountRepository;
import com.hglee.account.accounts.exception.NotFoundException;
import com.hglee.account.core.IEventPublisher;

@Service
public class RequestPasswordResetService implements RequestPasswordResetUseCase {
	private final IAccountRepository accountRepository;
	private final IEventPublisher eventPublisher;

	public RequestPasswordResetService(IAccountRepository accountRepository, IEventPublisher eventPublisher) {
		this.accountRepository = accountRepository;
		this.eventPublisher = eventPublisher;
	}

	@Override
	@Transactional
	public void execute(RequestPasswordResetCommand command) {
		String mobile = command.getMobile();

		Account account = accountRepository.findByMobile(mobile)
				.orElseThrow(() -> new NotFoundException("계정이 존재하지 않습니다."));

		if (!account.isSignedUp()) {
			throw new IllegalArgumentException("비밀번호 재설정을 요청할 수 없습니다.");
		}

		account.requestPasswordReset();

		Account createdAccount = this.accountRepository.save(account);

		this.eventPublisher.publish(
				new RequestedPasswordResetEvent(createdAccount, createdAccount.getPasswordResetRequest()));
	}
}
