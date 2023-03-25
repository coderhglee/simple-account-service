package com.hglee.account.accounts.application.in.usecase;

import com.hglee.account.accounts.application.in.command.SignInWithEmailCommand;
import com.hglee.account.accounts.dto.AccountResponseDto;

public interface SignInWithEmailUseCase {
	AccountResponseDto execute(SignInWithEmailCommand command);
}
