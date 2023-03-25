package com.hglee.account.accounts.application.in.usecase;

import com.hglee.account.accounts.application.in.command.SignUpWithMobileAndEmailCommand;
import com.hglee.account.accounts.dto.AccountResponseDto;

public interface SignUpWithMobileAndEmailUseCase {
	AccountResponseDto execute(SignUpWithMobileAndEmailCommand command);
}
