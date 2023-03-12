package com.hglee.account.auth.dto;

public class SignInWithMobileRequest {
	private String mobile;
	private String password;

	public SignInWithMobileRequest(String mobile, String password) {
		this.mobile = mobile;
		this.password = password;
	}

	public SignInWithMobileRequest() {
	}

	public String getMobile() {
		return mobile;
	}

	public String getPassword() {
		return password;
	}
}
