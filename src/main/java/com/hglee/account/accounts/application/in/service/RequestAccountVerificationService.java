package com.hglee.account.accounts.application.in.service;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.hglee.account.accounts.application.in.command.RequestAccountVerificationCommand;
import com.hglee.account.accounts.application.in.usecase.RequestAccountVerificationUseCase;
import com.hglee.account.accounts.domain.Account;
import com.hglee.account.accounts.domain.event.RequestedAccountVerificationEvent;
import com.hglee.account.accounts.domain.repository.IAccountRepository;
import com.hglee.account.accounts.dto.CreateVerificationCodeResponse;
import com.hglee.account.accounts.exception.ConflictException;
import com.hglee.account.accounts.infrastructure.client.IVerificationCodeClient;
import com.hglee.account.core.IEventPublisher;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RequestAccountVerificationService implements RequestAccountVerificationUseCase {
	private final IAccountRepository accountRepository;
	private final IEventPublisher eventPublisher;
	private final IVerificationCodeClient verificationCodeClient;

	@Override
	@Transactional
	public void execute(RequestAccountVerificationCommand command) {
		String mobile = command.getMobile();

		accountRepository.findByMobile(mobile).filter(Account::isSignedUp).ifPresent(account -> {
			throw new ConflictException("이미 가입된 계정입니다.");
		});

		CreateVerificationCodeResponse codeResponse = verificationCodeClient.create(mobile);

		this.eventPublisher.publish(new RequestedAccountVerificationEvent(mobile, codeResponse.getCode()));
	}
}
