package com.hglee.account.accounts.application.command;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.hglee.account.core.SelfValidator;

public class ResetPasswordCommand extends SelfValidator<ResetPasswordCommand> {
	@NotNull
	@Pattern(regexp = "^\\d{3}(\\d{4})\\d{4}$", message = "전화번호 형식이 올바르지 않습니다.")
	private final String mobile;
	@NotNull
	@Pattern(regexp = "^[0-9]{6}$", message = "인증코드 형식이 올바르지 않습니다.")
	private final String code;

	@NotNull
	@Pattern(regexp = "^[A-Za-z0-9\\w~@#$%^&*+=`|{}:;!.?\\\"()\\[\\]-]{6,18}$", message = "패스워드 형식이 올바르지 않습니다. 최소 6글자 최대 18글자 영문(대소문자), 숫자, 특수문자를 허용합니다.")
	private final String password;

	@NotNull
	@Pattern(regexp = "^[A-Za-z0-9\\w~@#$%^&*+=`|{}:;!.?\\\"()\\[\\]-]{6,18}$", message = "패스워드 형식이 올바르지 않습니다. 최소 6글자 최대 18글자 영문(대소문자), 숫자, 특수문자를 허용합니다.")
	private final String passwordConfirm;

	public ResetPasswordCommand(String mobile, String code, String password, String passwordConfirm) {
		this.mobile = mobile;
		this.code = code;
		this.password = password;
		this.passwordConfirm = passwordConfirm;

		this.validateSelf();
	}

	public String getMobile() {
		return mobile;
	}

	public String getCode() {
		return code;
	}

	public String getPassword() {
		return password;
	}

	public String getPasswordConfirm() {
		return passwordConfirm;
	}
}
