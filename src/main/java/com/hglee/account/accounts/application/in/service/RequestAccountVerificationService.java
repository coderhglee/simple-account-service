package com.hglee.account.accounts.application.in.service;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.hglee.account.accounts.application.in.command.RequestAccountVerificationCommand;
import com.hglee.account.accounts.application.in.usecase.RequestAccountVerificationUseCase;
import com.hglee.account.accounts.domain.Account;
import com.hglee.account.accounts.domain.event.RequestedAccountVerificationEvent;
import com.hglee.account.accounts.domain.repository.IAccountRepository;
import com.hglee.account.accounts.exception.ConflictException;
import com.hglee.account.core.IEventPublisher;
import com.hglee.account.verificationCode.application.command.CreateVerificationCodeCommand;
import com.hglee.account.verificationCode.application.usecase.CreateVerificationCodeUseCase;
import com.hglee.account.verificationCode.dto.CreateVerificationCodeResponse;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RequestAccountVerificationService implements RequestAccountVerificationUseCase {
	private final IAccountRepository accountRepository;
	private final IEventPublisher eventPublisher;
	private final CreateVerificationCodeUseCase createVerificationCodeUseCase;

	@Override
	@Transactional
	public void execute(RequestAccountVerificationCommand command) {
		String mobile = command.getMobile();

		accountRepository.findByMobile(mobile).filter(Account::isSignedUp).ifPresent(account -> {
			throw new ConflictException("이미 가입된 계정입니다.");
		});

		CreateVerificationCodeResponse codeResponse = createVerificationCodeUseCase.execute(
				new CreateVerificationCodeCommand(mobile));

		this.eventPublisher.publish(new RequestedAccountVerificationEvent(mobile, codeResponse.getCode()));
	}
}
