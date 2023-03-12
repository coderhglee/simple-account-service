package com.hglee.account.verificationCode.application.usecase;

import com.hglee.account.verificationCode.application.command.CreateVerificationCodeCommand;
import com.hglee.account.verificationCode.dto.CreateVerificationCodeResponse;

public interface CreateVerificationCodeUseCase {
	CreateVerificationCodeResponse execute(CreateVerificationCodeCommand command);
}
