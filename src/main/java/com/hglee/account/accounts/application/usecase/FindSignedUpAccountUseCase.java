package com.hglee.account.accounts.application.usecase;

import com.hglee.account.accounts.dto.AccountResponseDto;

public interface FindSignedUpAccountUseCase {
	AccountResponseDto execute(String accountId);
}
