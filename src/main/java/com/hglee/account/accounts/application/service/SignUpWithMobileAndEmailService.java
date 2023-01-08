package com.hglee.account.accounts.application.service;

import javax.transaction.Transactional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hglee.account.accounts.application.command.SignUpWithMobileAndEmailCommand;
import com.hglee.account.accounts.application.usecase.SignUpWithMobileAndEmailUseCase;
import com.hglee.account.accounts.domain.Account;
import com.hglee.account.accounts.domain.repository.IAccountRepository;
import com.hglee.account.accounts.dto.AccountResponseDto;
import com.hglee.account.accounts.exception.ConflictException;

@Service
public class SignUpWithMobileAndEmailService implements SignUpWithMobileAndEmailUseCase {
	private final IAccountRepository accountRepository;

	private final PasswordEncoder passwordEncoder;

	public SignUpWithMobileAndEmailService(IAccountRepository accountRepository, PasswordEncoder passwordEncoder) {
		this.accountRepository = accountRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	@Transactional
	public AccountResponseDto execute(SignUpWithMobileAndEmailCommand command) {
		String email = command.getEmail();
		String mobile = command.getMobile();

		checkAlreadyExistAccountByEmail(email);

		Account account = this.accountRepository.findByMobile(mobile)
				.orElseThrow(() -> new IllegalArgumentException("인증된 계정이 존재하지 않습니다."));

		if (account.isSignedUp()) {
			throw new ConflictException("이미 동일한 전화번호로 가입된 계정입니다.");
		}

		if (!account.isVerified()) {
			throw new IllegalArgumentException("전화번호 인증이 완료되지 않았습니다.");
		}

		String encodedPassword = passwordEncoder.encode(command.getPassword());

		account.signUpWithMobileAndEmail(email, encodedPassword, command.getName(), command.getNickName());

		this.accountRepository.save(account);

		return new AccountResponseDto(account.getId(), account.getMobile(), account.getStatusAsString(),
				account.getEmail(), account.getName(), account.getNickName());
	}

	private void checkAlreadyExistAccountByEmail(String email) {
		this.accountRepository.findByEmail(email).ifPresent((account) -> {
			if (account.isSignedUp()) {
				throw new ConflictException("이미 동일한 이메일로 가입된 계정입니다.");
			}
		});
	}
}
