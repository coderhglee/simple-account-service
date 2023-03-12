package com.hglee.account.verificationCode.application.command;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.hglee.account.core.SelfValidator;

import lombok.Getter;

@Getter
public class CreateVerificationCodeCommand extends SelfValidator<CreateVerificationCodeCommand> {
	@NotNull
	@Pattern(regexp = "^\\d{3}(\\d{4})\\d{4}$", message = "전화번호 형식이 올바르지 않습니다.")
	private final String mobile;

	public CreateVerificationCodeCommand(String mobile) {
		this.mobile = mobile;

		this.validateSelf();
	}
}
