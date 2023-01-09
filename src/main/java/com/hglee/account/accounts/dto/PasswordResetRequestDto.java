package com.hglee.account.accounts.dto;

public class PasswordResetRequestDto {
	private String mobile;
	private String code;
	private String password;
	private String passwordConfirm;

	public PasswordResetRequestDto() {
	}

	public PasswordResetRequestDto(String mobile, String code, String password, String passwordConfirm) {
		this.mobile = mobile;
		this.code = code;
		this.password = password;
		this.passwordConfirm = passwordConfirm;
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
