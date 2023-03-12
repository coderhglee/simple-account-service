package com.hglee.account.accounts.domain.event;

import com.hglee.account.accounts.domain.Account;
import com.hglee.account.core.IDomainEvent;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RequestedPasswordResetEvent implements IDomainEvent {
	private final Account account;

	private final String code;
}
