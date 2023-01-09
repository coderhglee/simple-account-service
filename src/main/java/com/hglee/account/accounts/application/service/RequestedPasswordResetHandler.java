package com.hglee.account.accounts.application.service;

import org.springframework.stereotype.Component;

import com.hglee.account.accounts.domain.Account;
import com.hglee.account.accounts.domain.PasswordResetRequest;
import com.hglee.account.accounts.domain.event.RequestedPasswordResetEvent;
import com.hglee.account.common.EventHandler;

@Component
public class RequestedPasswordResetHandler extends EventHandler<RequestedPasswordResetEvent> {
	@Override
	public void handle(RequestedPasswordResetEvent event) {
		Account account = event.getAccount();
		PasswordResetRequest passwordResetRequest = event.getPasswordResetRequest();

		System.out.format("비밀번호 재설정 인증번호가 전송되었습니다. mobile: %s code: %s expiresAt: %s", account.getMobile(),
				passwordResetRequest.getCode(), passwordResetRequest.getExpiresAt().toString());
	}
}
