package com.hglee.account.verificationCode.application.service;

import org.springframework.stereotype.Service;

import com.hglee.account.verificationCode.application.command.CreateVerificationCodeCommand;
import com.hglee.account.verificationCode.application.usecase.CreateVerificationCodeUseCase;
import com.hglee.account.verificationCode.domain.VerificationCode;
import com.hglee.account.verificationCode.domain.repository.IVerificationCodeRepository;
import com.hglee.account.verificationCode.dto.CreateVerificationCodeResponse;

@Service
public class CreateVerificationCodeService implements CreateVerificationCodeUseCase {

	private final IVerificationCodeRepository verificationCodeRepository;

	public CreateVerificationCodeService(IVerificationCodeRepository verificationCodeRepository) {
		this.verificationCodeRepository = verificationCodeRepository;
	}

	@Override
	public CreateVerificationCodeResponse execute(CreateVerificationCodeCommand command) {
		VerificationCode createdVerificationCode = this.verificationCodeRepository.save(
				VerificationCode.generate(command.getIdentifier()));

		return CreateVerificationCodeResponse.of(createdVerificationCode.getCode());
	}
}
