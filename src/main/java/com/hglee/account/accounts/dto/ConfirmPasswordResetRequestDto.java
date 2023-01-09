package com.hglee.account.accounts.dto;

public class ConfirmPasswordResetRequestDto {
	String mobile;
	String code;

	public ConfirmPasswordResetRequestDto() {
	}

	public ConfirmPasswordResetRequestDto(String mobile, String code) {
		this.mobile = mobile;
		this.code = code;
	}

	public String getMobile() {
		return mobile;
	}

	public String getCode() {
		return code;
	}
}
