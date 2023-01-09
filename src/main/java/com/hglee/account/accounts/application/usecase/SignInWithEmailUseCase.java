package com.hglee.account.accounts.application.usecase;

import com.hglee.account.accounts.application.command.SignInWithEmailCommand;
import com.hglee.account.accounts.dto.AccountResponseDto;

public interface SignInWithEmailUseCase {
	AccountResponseDto execute(SignInWithEmailCommand command);
}
