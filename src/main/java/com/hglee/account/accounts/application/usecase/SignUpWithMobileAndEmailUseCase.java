package com.hglee.account.accounts.application.usecase;

import com.hglee.account.accounts.application.command.SignUpWithMobileAndEmailCommand;
import com.hglee.account.accounts.dto.AccountResponseDto;

public interface SignUpWithMobileAndEmailUseCase {
	AccountResponseDto execute(SignUpWithMobileAndEmailCommand command);
}
