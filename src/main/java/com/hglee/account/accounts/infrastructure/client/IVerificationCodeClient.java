package com.hglee.account.accounts.infrastructure.client;

import com.hglee.account.accounts.dto.CreateVerificationCodeResponse;

public interface IVerificationCodeClient {
	CreateVerificationCodeResponse create(String identifier);

	boolean verify(String identifier, String code);
}
