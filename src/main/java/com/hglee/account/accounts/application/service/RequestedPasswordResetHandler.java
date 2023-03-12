package com.hglee.account.accounts.application.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.hglee.account.accounts.domain.Account;
import com.hglee.account.accounts.domain.event.RequestedPasswordResetEvent;
import com.hglee.account.common.EventHandler;

@Component
public class RequestedPasswordResetHandler extends EventHandler<RequestedPasswordResetEvent> {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public void handle(RequestedPasswordResetEvent event) {
		Account account = event.getAccount();

		log.info("비밀번호 재설정 인증번호가 전송되었습니다. mobile: {} code: {}", account.getMobile(),
				event.getCode());
	}
}
