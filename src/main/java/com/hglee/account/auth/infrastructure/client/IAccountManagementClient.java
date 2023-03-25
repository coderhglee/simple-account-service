package com.hglee.account.auth.infrastructure.client;

import com.hglee.account.auth.dto.AccountResponseDto;
import com.hglee.account.auth.dto.SignUpMobileRequest;

public interface IAccountManagementClient {
	AccountResponseDto signInWithMobile(String mobile, String password);

	AccountResponseDto signInWithEmail(String email, String password);

	AccountResponseDto findById(String id);

	void requestAccountVerificationMobile(String mobile);

	void verifyMobile(String mobile, String code);

	AccountResponseDto signUpWithMobile(SignUpMobileRequest request);

}
