package com.hglee.account.accounts.dto;

public class RequestPasswordResetRequestDto {
	String mobile;

	public RequestPasswordResetRequestDto() {
	}

	public RequestPasswordResetRequestDto(String mobile) {
		this.mobile = mobile;
	}

	public String getMobile() {
		return mobile;
	}
}
