package com.hglee.account.verificationCode.application.command;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.hglee.account.core.SelfValidator;

import lombok.Getter;

@Getter
public class VerifyVerificationCodeCommand extends SelfValidator<VerifyVerificationCodeCommand> {
	@NotNull
	@Pattern(regexp = "^\\d{3}(\\d{4})\\d{4}$", message = "전화번호 형식이 올바르지 않습니다.")
	private final String mobile;
	@NotNull
	@Pattern(regexp = "^[0-9]{6}$", message = "인증코드 형식이 올바르지 않습니다.")
	private final String code;

	public VerifyVerificationCodeCommand(String mobile, String code) {
		this.mobile = mobile;
		this.code = code;

		this.validateSelf();
	}
}
