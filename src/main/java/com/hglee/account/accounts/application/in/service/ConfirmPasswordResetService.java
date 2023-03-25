package com.hglee.account.accounts.application.in.service;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.hglee.account.accounts.application.in.command.ConfirmPasswordResetCommand;
import com.hglee.account.accounts.application.in.usecase.ConfirmPasswordResetUseCase;
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
