package com.hglee.account.accounts.application.in.usecase;

import com.hglee.account.accounts.application.in.command.RequestPasswordResetCommand;

public interface RequestPasswordResetUseCase {
	void execute(RequestPasswordResetCommand command);
}
