package com.hglee.account.accounts.application.usecase;

import com.hglee.account.accounts.application.command.VerifyAccountCommand;

public interface VerifyAccountUseCase {
	void execute(VerifyAccountCommand command);
}
