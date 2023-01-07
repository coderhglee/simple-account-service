package com.hglee.account.accounts.dto;

public class RequestAccountVerificationMobileRequest {
	private String mobile;

	public RequestAccountVerificationMobileRequest() {
	}

	public RequestAccountVerificationMobileRequest(String mobile) {
		this.mobile = mobile;
	}

	public String getMobile() {
		return mobile;
	}
}
