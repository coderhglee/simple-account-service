package com.hglee.account.accounts.application.usecase;

import com.hglee.account.accounts.application.command.RequestAccountVerificationCommand;

public interface RequestAccountVerificationUseCase {
	void execute(RequestAccountVerificationCommand command);
}
