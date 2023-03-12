package com.hglee.account.verificationCode.application.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.hglee.account.verificationCode.application.usecase.FindVerificationCodeUseCase;
import com.hglee.account.verificationCode.domain.VerificationCode;
import com.hglee.account.verificationCode.domain.VerificationCodeId;
import com.hglee.account.verificationCode.domain.repository.IVerificationCodeRepository;

@Service
public class FindVerificationCodeService implements FindVerificationCodeUseCase {
	private final IVerificationCodeRepository verificationCodeRepository;

	public FindVerificationCodeService(IVerificationCodeRepository verificationCodeRepository) {
		this.verificationCodeRepository = verificationCodeRepository;
	}

	@Override
	public Optional<VerificationCode> findVerified(String mobile, String code) {
		return this.verificationCodeRepository.findOne(new VerificationCodeId(mobile, code)).filter(VerificationCode::isConfirmed);
	}
}
