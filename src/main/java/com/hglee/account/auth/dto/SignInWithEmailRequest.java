package com.hglee.account.auth.dto;

public class SignInWithEmailRequest {
	private String email;
	private String password;

	public SignInWithEmailRequest(String email, String password) {
		this.email = email;
		this.password = password;
	}

	public SignInWithEmailRequest() {
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}
}
