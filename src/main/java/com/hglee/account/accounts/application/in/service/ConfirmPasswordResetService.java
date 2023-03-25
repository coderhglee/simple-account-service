package com.hglee.account.accounts.application.in.service;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.hglee.account.accounts.application.in.command.ConfirmPasswordResetCommand;
import com.hglee.account.accounts.application.in.usecase.ConfirmPasswordResetUseCase;
import com.hglee.account.accounts.infrastructure.client.IVerificationCodeClient;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ConfirmPasswordResetService implements ConfirmPasswordResetUseCase {
	private final IVerificationCodeClient verificationCodeClient;

	@Override
	@Transactional
	public void execute(ConfirmPasswordResetCommand command) {
		if (!this.verificationCodeClient.verify(command.getMobile(), command.getCode())) {
			throw new IllegalArgumentException("인증코드를 사용할 수 없습니다.");
		}
	}
}
