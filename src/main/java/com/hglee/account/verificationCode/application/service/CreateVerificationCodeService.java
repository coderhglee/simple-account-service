package com.hglee.account.verificationCode.application.service;

import org.springframework.stereotype.Service;

import com.hglee.account.verificationCode.application.command.CreateVerificationCodeCommand;
import com.hglee.account.verificationCode.application.usecase.CreateVerificationCodeUseCase;
import com.hglee.account.verificationCode.domain.VerificationCode;
import com.hglee.account.verificationCode.domain.repository.IVerificationCodeRepository;

@Service
public class CreateVerificationCodeService implements CreateVerificationCodeUseCase {

	private final IVerificationCodeRepository verificationCodeRepository;

	public CreateVerificationCodeService(IVerificationCodeRepository verificationCodeRepository) {
		this.verificationCodeRepository = verificationCodeRepository;
	}

	@Override
	public VerificationCode execute(CreateVerificationCodeCommand command) {
		VerificationCode verificationCode = VerificationCode.generateCode(command.getMobile());

		return this.verificationCodeRepository.save(verificationCode);
	}
}
