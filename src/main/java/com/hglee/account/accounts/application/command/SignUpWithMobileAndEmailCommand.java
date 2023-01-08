package com.hglee.account.accounts.application.command;

public class SignUpWithMobileAndEmailCommand {
	private final String mobile;
	private final String email;
	private final String password;
	private final String name;
	private final String nickName;

	public SignUpWithMobileAndEmailCommand(String mobile, String email, String password, String name, String nickName) {
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
