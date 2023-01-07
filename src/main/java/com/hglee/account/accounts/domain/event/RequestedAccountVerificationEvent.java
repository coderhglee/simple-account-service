package com.hglee.account.accounts.domain.event;

import com.hglee.account.accounts.domain.Account;
import com.hglee.account.accounts.domain.PinCode;
import com.hglee.account.core.IDomainEvent;

public class RequestedAccountVerificationEvent implements IDomainEvent {
	private final Account account;
	private final PinCode pinCode;

	public RequestedAccountVerificationEvent(Account account, PinCode pinCode) {
		this.account = account;
		this.pinCode = pinCode;
	}

	public Account getAccount() {
		return account;
	}

	public PinCode getPinCode() {
		return pinCode;
	}
}
