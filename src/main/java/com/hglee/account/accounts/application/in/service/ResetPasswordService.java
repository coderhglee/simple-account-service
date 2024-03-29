package com.hglee.account.accounts.application.in.service;

import javax.transaction.Transactional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hglee.account.accounts.application.in.command.ResetPasswordCommand;
import com.hglee.account.accounts.application.in.usecase.ResetPasswordUseCase;
import com.hglee.account.accounts.domain.Account;
import com.hglee.account.accounts.domain.repository.IAccountRepository;
import com.hglee.account.accounts.exception.NotFoundException;
import com.hglee.account.verificationCode.application.usecase.FindVerificationCodeUseCase;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ResetPasswordService implements ResetPasswordUseCase {
	private final IAccountRepository accountRepository;
	private final FindVerificationCodeUseCase findVerificationCodeUseCase;
	private final PasswordEncoder encoder;

	@Override
	@Transactional
	public void execute(ResetPasswordCommand command) {
		String newPassword = command.getPassword();
		String passwordConfirm = command.getPasswordConfirm();
		String mobile = command.getMobile();

		if (!newPassword.equals(passwordConfirm)) {
			throw new IllegalArgumentException("패스워드가 일치하지 않습니다.");
		}

		Account account = this.accountRepository.findByMobile(mobile)
				.orElseThrow(() -> new NotFoundException("계정이 존재하지 않습니다."));

		if (encoder.matches(newPassword, account.getPassword())) {
			throw new IllegalArgumentException("이전 패스워드와 일치합니다.");
		}

		findVerificationCodeUseCase.findVerified(mobile, command.getCode()).orElseThrow(() -> {
			throw new IllegalArgumentException("잘못된 인증코드 입니다.");
		});

		account.resetPassword(encoder.encode(newPassword));

		this.accountRepository.save(account);
	}
}
