package com.hglee.account.accounts.application.usecase;

import com.hglee.account.accounts.application.command.SignInWithMobileCommand;
import com.hglee.account.accounts.dto.AccountResponseDto;

public interface SignInWithMobileUseCase {
	AccountResponseDto execute(SignInWithMobileCommand command);
}
