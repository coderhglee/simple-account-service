package com.hglee.account.accounts.application.out.usecase;

import com.hglee.account.accounts.dto.AccountResponseDto;
import com.hglee.account.accounts.dto.SignUpMobileRequest;

public interface SignUpAccountUseCase {
	void requestAccountVerificationMobile(String mobile);

	void verifyMobile(String mobile, String code);

	AccountResponseDto signUpWithMobile(SignUpMobileRequest request);
}
