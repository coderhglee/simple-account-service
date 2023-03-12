package com.hglee.account.verificationCode.domain.repository;

import java.util.Optional;

import com.hglee.account.verificationCode.domain.VerificationCode;
import com.hglee.account.verificationCode.domain.VerificationCodeId;

public interface IVerificationCodeRepository {
	VerificationCode save(VerificationCode verificationCode);

	Optional<VerificationCode> findOne(VerificationCodeId id);
}
