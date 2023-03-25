package com.hglee.account.accounts.application.in.service;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.hglee.account.accounts.application.in.command.VerifyAccountCommand;
import com.hglee.account.accounts.application.in.usecase.VerifyAccountUseCase;
import com.hglee.account.verificationCode.application.command.VerifyVerificationCodeCommand;
import com.hglee.account.verificationCode.application.usecase.VerifyVerificationCodeUseCase;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class VerifyAccountService implements VerifyAccountUseCase {

	private final VerifyVerificationCodeUseCase verifyVerificationCodeUseCase;

	@Override
	@Transactional
	public void execute(VerifyAccountCommand command) {
		this.verifyVerificationCodeUseCase.execute(
				new VerifyVerificationCodeCommand(command.getMobile(), command.getCode()));
	}
}
