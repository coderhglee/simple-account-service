package com.hglee.account.accounts.application.service;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.hglee.account.accounts.application.command.ConfirmPasswordResetCommand;
import com.hglee.account.accounts.application.usecase.ConfirmPasswordResetUseCase;
import com.hglee.account.accounts.domain.Account;
import com.hglee.account.accounts.domain.repository.IAccountRepository;
import com.hglee.account.accounts.exception.NotFoundException;

@Service
public class ConfirmPasswordResetService implements ConfirmPasswordResetUseCase {
	private final IAccountRepository accountRepository;

	public ConfirmPasswordResetService(IAccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	@Override
	@Transactional
	public void execute(ConfirmPasswordResetCommand command) {
		String mobile = command.getMobile();

		Account account = this.accountRepository.findByMobile(mobile)
				.orElseThrow(() -> new NotFoundException("계정이 존재하지 않습니다."));

		if (!account.hasPasswordResetRequest()) {
			throw new IllegalArgumentException("요청된 인증코드가 없습니다.");
		}
		if (!account.isSamePasswordResetCode(command.getCode())) {
			throw new IllegalArgumentException("인증코드가 일치하지 않습니다.");
		}

		if (account.isExpiredPasswordResetCode()) {
			throw new IllegalArgumentException("인증코드가 만료되었습니다.");
		}

		account.confirmPasswordReset();

		this.accountRepository.save(account);
	}
}
