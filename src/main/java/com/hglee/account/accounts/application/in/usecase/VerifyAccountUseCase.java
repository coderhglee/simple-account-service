package com.hglee.account.accounts.application.in.usecase;

import com.hglee.account.accounts.application.in.command.VerifyAccountCommand;

public interface VerifyAccountUseCase {
	void execute(VerifyAccountCommand command);
}
