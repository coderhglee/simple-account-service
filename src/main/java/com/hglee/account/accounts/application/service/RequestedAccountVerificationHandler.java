package com.hglee.account.accounts.application.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.hglee.account.accounts.domain.event.RequestedAccountVerificationEvent;
import com.hglee.account.common.EventHandler;

@Component
public class RequestedAccountVerificationHandler extends EventHandler<RequestedAccountVerificationEvent> {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public void handle(RequestedAccountVerificationEvent event) {
		log.info("인증번호가 전송되었습니다. mobile: {} code: {}", event.getMobile(), event.getCode());
	}
}
