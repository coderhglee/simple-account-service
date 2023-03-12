package com.hglee.account.verificationCode.infrastructure.persistence;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.hglee.account.verificationCode.domain.VerificationCode;
import com.hglee.account.verificationCode.domain.VerificationCodeId;
import com.hglee.account.verificationCode.domain.repository.IVerificationCodeRepository;

@Component
public class VerificationCodeRepository implements IVerificationCodeRepository {
	private final VerificationCodeJpaRepository repository;

	public VerificationCodeRepository(VerificationCodeJpaRepository repository) {
		this.repository = repository;
	}

	@Override
	public VerificationCode save(VerificationCode verificationCode) {
		return this.repository.save(verificationCode);
	}

	@Override
	public Optional<VerificationCode> findOne(VerificationCodeId id) {
		return this.repository.findById(id);
	}
}
