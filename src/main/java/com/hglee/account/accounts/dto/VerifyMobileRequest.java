package com.hglee.account.accounts.dto;

public class VerifyMobileRequest {
	private String mobile;
	private String code;

	public VerifyMobileRequest() {
	}

	public VerifyMobileRequest(String mobile, String code) {
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
