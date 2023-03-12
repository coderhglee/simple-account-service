package com.hglee.account.accounts.application.service;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.hglee.account.accounts.application.command.ConfirmPasswordResetCommand;
import com.hglee.account.accounts.application.usecase.ConfirmPasswordResetUseCase;
import com.hglee.account.verificationCode.application.command.VerifyVerificationCodeCommand;
import com.hglee.account.verificationCode.application.usecase.VerifyVerificationCodeUseCase;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ConfirmPasswordResetService implements ConfirmPasswordResetUseCase {
	private final VerifyVerificationCodeUseCase verifyVerificationCodeUseCase;

	@Override
	@Transactional
	public void execute(ConfirmPasswordResetCommand command) {
		this.verifyVerificationCodeUseCase.execute(
				new VerifyVerificationCodeCommand(command.getMobile(), command.getCode()));
	}
}
