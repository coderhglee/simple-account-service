package com.hglee.account.accounts.application.usecase;

import com.hglee.account.accounts.dto.AccountResponseDto;

public interface IAccountService {
	AccountResponseDto signInWithMobile(String mobile, String password);
	AccountResponseDto signInWithEmail(String email, String password);
	AccountResponseDto findById(String id);
}
