package com.hglee.account.accounts.application.in.command;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.hglee.account.core.SelfValidator;

public class RequestAccountVerificationCommand extends SelfValidator<RequestAccountVerificationCommand> {
	@NotNull
	@Pattern(regexp = "^\\d{3}(\\d{4})\\d{4}$", message = "전화번호 형식이 올바르지 않습니다.")
	private final String mobile;

	public RequestAccountVerificationCommand(String mobile) {
		this.mobile = mobile;

		this.validateSelf();
	}

	public String getMobile() {
		return mobile;
	}
}
