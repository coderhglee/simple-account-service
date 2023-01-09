package com.hglee.account.accounts.application.usecase;

import com.hglee.account.accounts.application.command.RequestPasswordResetCommand;

public interface RequestPasswordResetUseCase {
	void execute(RequestPasswordResetCommand command);
}
