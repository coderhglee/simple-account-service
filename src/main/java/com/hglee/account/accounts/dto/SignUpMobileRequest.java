package com.hglee.account.accounts.dto;

public class SignUpMobileRequest {
	private String mobile;
	private String email;
	private String password;
	private String name;
	private String nickName;

	public SignUpMobileRequest() {
	}

	public SignUpMobileRequest(String mobile, String email, String password, String name, String nickName) {
		this.mobile = mobile;
		this.email = email;
		this.password = password;
		this.name = name;
		this.nickName = nickName;
	}

	public String getMobile() {
		return mobile;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public String getName() {
		return name;
	}

	public String getNickName() {
		return nickName;
	}
}
