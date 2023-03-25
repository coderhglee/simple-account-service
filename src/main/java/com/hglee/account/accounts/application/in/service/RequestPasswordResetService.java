package com.hglee.account.accounts.application.in.service;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.hglee.account.accounts.application.in.command.RequestPasswordResetCommand;
import com.hglee.account.accounts.application.in.usecase.RequestPasswordResetUseCase;
import com.hglee.account.accounts.domain.Account;
import com.hglee.account.accounts.domain.event.RequestedPasswordResetEvent;
import com.hglee.account.accounts.domain.repository.IAccountRepository;
import com.hglee.account.accounts.exception.NotFoundException;
import com.hglee.account.core.IEventPublisher;
import com.hglee.account.verificationCode.application.command.CreateVerificationCodeCommand;
import com.hglee.account.verificationCode.application.usecase.CreateVerificationCodeUseCase;
import com.hglee.account.verificationCode.dto.CreateVerificationCodeResponse;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class RequestPasswordResetService implements RequestPasswordResetUseCase {
	private final IAccountRepository accountRepository;
	private final IEventPublisher eventPublisher;
	private final CreateVerificationCodeUseCase createVerificationCodeUseCase;

	@Override
	@Transactional
	public void execute(RequestPasswordResetCommand command) {
		String mobile = command.getMobile();

		Account account = accountRepository.findByMobile(mobile)
				.orElseThrow(() -> new NotFoundException("계정이 존재하지 않습니다."));

		if (!account.isSignedUp()) {
			throw new IllegalArgumentException("비밀번호 재설정을 요청할 수 없습니다.");
		}

		CreateVerificationCodeResponse createVerificationCodeResponse = createVerificationCodeUseCase.execute(
				new CreateVerificationCodeCommand(mobile));

		this.eventPublisher.publish(
				new RequestedPasswordResetEvent(account, createVerificationCodeResponse.getCode()));
	}
}
