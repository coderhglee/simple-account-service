package com.hglee.account.accounts.infrastructure.client;

import org.springframework.stereotype.Component;

import com.hglee.account.accounts.dto.CreateVerificationCodeResponse;
import com.hglee.account.verificationCode.application.command.CreateVerificationCodeCommand;
import com.hglee.account.verificationCode.application.command.VerifyVerificationCodeCommand;
import com.hglee.account.verificationCode.application.usecase.CreateVerificationCodeUseCase;
import com.hglee.account.verificationCode.application.usecase.VerifyVerificationCodeUseCase;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class VerificationCodeClient implements IVerificationCodeClient {
	private final CreateVerificationCodeUseCase createVerificationCodeUseCase;

	private final VerifyVerificationCodeUseCase verifyVerificationCodeUseCase;

	@Override
	public CreateVerificationCodeResponse create(String identifier) {
		com.hglee.account.verificationCode.dto.CreateVerificationCodeResponse response = createVerificationCodeUseCase.execute(
				new CreateVerificationCodeCommand(identifier));

		return new CreateVerificationCodeResponse(response.getCode());
	}

	@Override
	public boolean verify(String identifier, String code) {
		return this.verifyVerificationCodeUseCase.execute(new VerifyVerificationCodeCommand(identifier, code));
	}
}
