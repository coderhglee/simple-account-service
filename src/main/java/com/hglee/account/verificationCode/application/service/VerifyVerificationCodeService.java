package com.hglee.account.verificationCode.application.service;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.hglee.account.accounts.exception.NotFoundException;
import com.hglee.account.verificationCode.application.usecase.VerifyVerificationCodeUseCase;
import com.hglee.account.verificationCode.application.command.VerifyVerificationCodeCommand;
import com.hglee.account.verificationCode.domain.VerificationCode;
import com.hglee.account.verificationCode.domain.VerificationCodeId;
import com.hglee.account.verificationCode.domain.repository.IVerificationCodeRepository;

@Service
public class VerifyVerificationCodeService implements VerifyVerificationCodeUseCase {

	private final IVerificationCodeRepository verificationCodeRepository;

	public VerifyVerificationCodeService(IVerificationCodeRepository verificationCodeRepository) {
		this.verificationCodeRepository = verificationCodeRepository;
	}

	@Override
	@Transactional
	public boolean execute(VerifyVerificationCodeCommand command) {
		try {
			String identifier = command.getIdentifier();
			String code = command.getCode();

			VerificationCode verificationCode = this.verificationCodeRepository.findOne(
							VerificationCodeId.of(identifier, code))
					.orElseThrow(() -> new NotFoundException("해당 identifier로 요청된 인증코드가 존재하지 않습니다."));

			if (verificationCode.isExpired()) {
				throw new IllegalStateException("인증코드가 만료되었습니다.");
			}

			verificationCode.verify();

			this.verificationCodeRepository.save(verificationCode);

			return true;
		} catch (Exception exception) {
			return false;
		}
	}
}
