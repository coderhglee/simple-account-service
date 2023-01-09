package com.hglee.account.accounts.domain.event;

import com.hglee.account.accounts.domain.Account;
import com.hglee.account.accounts.domain.PasswordResetRequest;
import com.hglee.account.core.IDomainEvent;

public class RequestedPasswordResetEvent implements IDomainEvent {
	private final Account account;
	private final PasswordResetRequest passwordResetRequest;

	public RequestedPasswordResetEvent(Account account, PasswordResetRequest pinCode) {
		this.account = account;
		this.passwordResetRequest = pinCode;
	}

	public Account getAccount() {
		return account;
	}

	public PasswordResetRequest getPasswordResetRequest() {
		return passwordResetRequest;
	}
}
