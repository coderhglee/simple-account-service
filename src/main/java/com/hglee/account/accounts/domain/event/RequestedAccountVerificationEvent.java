package com.hglee.account.accounts.domain.event;

import com.hglee.account.accounts.domain.Account;
import com.hglee.account.core.IDomainEvent;

public class RequestedAccountVerificationEvent implements IDomainEvent {
	private final Account account;

	public RequestedAccountVerificationEvent(Account account) {
		this.account = account;
	}

	public Account getAccount() {
		return account;
	}
}
