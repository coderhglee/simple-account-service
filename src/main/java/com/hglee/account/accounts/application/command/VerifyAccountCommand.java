package com.hglee.account.accounts.application.command;

public class VerifyAccountCommand {
	private final String mobile;
	private final String code;

	public VerifyAccountCommand(String mobile, String code) {
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
