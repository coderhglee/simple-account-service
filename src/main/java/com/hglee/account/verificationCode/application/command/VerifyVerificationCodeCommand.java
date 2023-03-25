package com.hglee.account.verificationCode.application.command;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.hglee.account.core.SelfValidator;

import lombok.Getter;

@Getter
public class VerifyVerificationCodeCommand extends SelfValidator<VerifyVerificationCodeCommand> {
	@NotNull
	private final String identifier;
	@NotNull
	@Pattern(regexp = "^[0-9]{6}$", message = "인증코드 형식이 올바르지 않습니다.")
	private final String code;

	public VerifyVerificationCodeCommand(String identifier, String code) {
		this.identifier = identifier;
		this.code = code;

		this.validateSelf();
	}
}
