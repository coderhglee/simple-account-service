package com.hglee.account.accounts.application.command;

public class RequestAccountVerificationCommand {
	private final String mobile;

	public RequestAccountVerificationCommand(String mobile) {
		this.mobile = mobile;
	}

	public String getMobile() {
		return mobile;
	}
}
