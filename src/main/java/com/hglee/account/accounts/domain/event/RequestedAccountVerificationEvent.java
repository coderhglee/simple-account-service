package com.hglee.account.accounts.domain.event;

import com.hglee.account.core.IDomainEvent;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RequestedAccountVerificationEvent implements IDomainEvent {
	private final String mobile;
	private final String code;
}
