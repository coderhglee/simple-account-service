package com.hglee.account.accounts.application.usecase;

import com.hglee.account.accounts.application.command.ResetPasswordCommand;

public interface ResetPasswordUseCase {
	void execute(ResetPasswordCommand command);
}
