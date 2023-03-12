package com.hglee.account.verificationCode.application.usecase;

import com.hglee.account.verificationCode.application.command.VerifyVerificationCodeCommand;

public interface VerifyVerificationCodeUseCase {
	void execute(VerifyVerificationCodeCommand command);
}
