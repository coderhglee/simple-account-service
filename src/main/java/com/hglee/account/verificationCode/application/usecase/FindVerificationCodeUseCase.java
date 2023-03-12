package com.hglee.account.verificationCode.application.usecase;

import java.util.Optional;

import com.hglee.account.verificationCode.domain.VerificationCode;

public interface FindVerificationCodeUseCase {
	Optional<VerificationCode> findVerified(String mobile, String code);
}
