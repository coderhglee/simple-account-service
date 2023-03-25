package com.hglee.account.accounts.application.in.usecase;

import com.hglee.account.accounts.application.in.command.RequestAccountVerificationCommand;

public interface RequestAccountVerificationUseCase {
	void execute(RequestAccountVerificationCommand command);
}
