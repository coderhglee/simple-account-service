package com.hglee.account.accounts.application.in.usecase;

import com.hglee.account.accounts.application.in.command.ResetPasswordCommand;

public interface ResetPasswordUseCase {
	void execute(ResetPasswordCommand command);
}
