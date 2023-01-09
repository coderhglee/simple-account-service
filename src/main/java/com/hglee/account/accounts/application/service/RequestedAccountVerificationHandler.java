package com.hglee.account.accounts.application.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.hglee.account.accounts.domain.Account;
import com.hglee.account.accounts.domain.PinCode;
import com.hglee.account.accounts.domain.event.RequestedAccountVerificationEvent;
import com.hglee.account.common.EventHandler;

@Component
public class RequestedAccountVerificationHandler extends EventHandler<RequestedAccountVerificationEvent> {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public void handle(RequestedAccountVerificationEvent event) {
		Account account = event.getAccount();
		PinCode pinCode = event.getPinCode();

		log.info("인증번호가 전송되었습니다. mobile: {} code: {} expiresAt: {}", account.getMobile(), pinCode.getCode(),
				pinCode.getExpiresAt().toString());
	}
}
