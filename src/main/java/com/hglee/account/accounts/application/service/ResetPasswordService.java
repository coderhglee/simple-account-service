package com.hglee.account.accounts.application.service;

import javax.transaction.Transactional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hglee.account.accounts.application.command.ResetPasswordCommand;
import com.hglee.account.accounts.application.usecase.ResetPasswordUseCase;
import com.hglee.account.accounts.domain.Account;
import com.hglee.account.accounts.domain.repository.IAccountRepository;
import com.hglee.account.accounts.exception.NotFoundException;

@Service
public class ResetPasswordService implements ResetPasswordUseCase {
	private final IAccountRepository accountRepository;
	private final PasswordEncoder encoder;

	public ResetPasswordService(IAccountRepository accountRepository, PasswordEncoder encoder) {
		this.accountRepository = accountRepository;
		this.encoder = encoder;
	}

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

		if (!account.isConfirmedPasswordReset(command.getCode())) {
			throw new IllegalArgumentException("잘못된 인증코드 입니다.");
		}

		account.resetPassword(encoder.encode(newPassword));

		this.accountRepository.save(account);
	}
}
