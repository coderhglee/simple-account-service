package com.hglee.account.accounts.application.command;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.hglee.account.core.SelfValidator;

public class ConfirmPasswordResetCommand extends SelfValidator<ConfirmPasswordResetCommand> {
	@NotNull
	@Pattern(regexp = "^\\d{3}(\\d{4})\\d{4}$", message = "전화번호 형식이 올바르지 않습니다.")
	private final String mobile;
	@NotNull
	@Pattern(regexp = "^[0-9]{6}$", message = "인증코드 형식이 올바르지 않습니다.")
	private final String code;

	public ConfirmPasswordResetCommand(String mobile, String code) {
		this.mobile = mobile;
		this.code = code;

		this.validateSelf();
	}

	public String getMobile() {
		return mobile;
	}

	public String getCode() {
		return code;
	}
}
