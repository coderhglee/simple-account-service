package com.hglee.account.accounts.application.usecase;

import com.hglee.account.accounts.application.command.ConfirmPasswordResetCommand;

public interface ConfirmPasswordResetUseCase {
	void execute(ConfirmPasswordResetCommand command);
}
