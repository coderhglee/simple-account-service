package com.hglee.account.accounts.application.in.usecase;

import com.hglee.account.accounts.application.in.command.SignInWithMobileCommand;
import com.hglee.account.accounts.dto.AccountResponseDto;

public interface SignInWithMobileUseCase {
	AccountResponseDto execute(SignInWithMobileCommand command);
}
