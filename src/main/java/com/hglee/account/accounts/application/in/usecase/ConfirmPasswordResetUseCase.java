package com.hglee.account.accounts.application.in.usecase;

import com.hglee.account.accounts.application.in.command.ConfirmPasswordResetCommand;

public interface ConfirmPasswordResetUseCase {
	void execute(ConfirmPasswordResetCommand command);
}
