package com.hglee.account.accounts.application.command;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.hglee.account.core.SelfValidator;

public class SignInWithEmailCommand extends SelfValidator<SignInWithEmailCommand> {
	@NotNull
	@Pattern(regexp = "^([\\S\\d]+)@(\\S+)[.](\\S+.?\\S+)$", message = "이메일 형식이 올바르지 않습니다.")
	private final String email;

	@NotNull
	@Pattern(regexp = "^[A-Za-z0-9\\w~@#$%^&*+=`|{}:;!.?\\\"()\\[\\]-]{6,18}$", message = "패스워드 형식이 올바르지 않습니다. 최소 6글자 최대 18글자 영문(대소문자), 숫자, 특수문자를 허용합니다.")
	private final String password;

	public SignInWithEmailCommand(String email, String password) {
		this.email = email;
		this.password = password;

		this.validateSelf();
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}
}
