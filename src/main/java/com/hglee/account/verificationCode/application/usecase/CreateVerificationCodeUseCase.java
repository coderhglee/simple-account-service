package com.hglee.account.verificationCode.application.usecase;

import com.hglee.account.verificationCode.application.command.CreateVerificationCodeCommand;
import com.hglee.account.verificationCode.domain.VerificationCode;

public interface CreateVerificationCodeUseCase {
	VerificationCode execute(CreateVerificationCodeCommand command);
}
