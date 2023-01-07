package com.hglee.account.accounts.application.service;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.hglee.account.accounts.application.command.VerifyAccountCommand;
import com.hglee.account.accounts.application.usecase.VerifyAccountUseCase;
import com.hglee.account.accounts.domain.Account;
import com.hglee.account.accounts.domain.Status;
import com.hglee.account.accounts.domain.repository.IAccountRepository;

@Service
public class VerifyAccountService implements VerifyAccountUseCase {
	private final IAccountRepository accountRepository;

	public VerifyAccountService(IAccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	@Override
	@Transactional
	public void execute(VerifyAccountCommand command) {
		String mobile = command.getMobile();

		Account account = this.accountRepository.findByMobileAndStatus(mobile, Status.VERIFICATION_REQUESTED)
				.orElseThrow(() -> new IllegalArgumentException("해당 모바일로 요청된 인증코드가 존재하지 않습니다."));

		if (!account.isSamePinCode(command.getCode())) {
			throw new IllegalArgumentException("인증코드가 일치하지 않습니다.");
		}

		if (account.isExpiredPinCode()) {
			throw new IllegalArgumentException("인증코드가 만료되었습니다.");
		}

		account.verify();

		this.accountRepository.save(account);
	}
}
