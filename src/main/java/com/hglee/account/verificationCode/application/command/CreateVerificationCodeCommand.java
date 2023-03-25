package com.hglee.account.verificationCode.application.command;

import javax.validation.constraints.NotNull;

import com.hglee.account.core.SelfValidator;

import lombok.Getter;

@Getter
public class CreateVerificationCodeCommand extends SelfValidator<CreateVerificationCodeCommand> {
	@NotNull
	private final String identifier;

	public CreateVerificationCodeCommand(String identifier) {
		this.identifier = identifier;

		this.validateSelf();
	}
}
