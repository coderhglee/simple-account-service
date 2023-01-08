package com.hglee.account.accounts.application.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hglee.account.accounts.application.command.SignInWithEmailCommand;
import com.hglee.account.accounts.application.command.SignInWithMobileCommand;
import com.hglee.account.accounts.application.usecase.SignInWithEmailUseCase;
import com.hglee.account.accounts.application.usecase.SignInWithMobileUseCase;
import com.hglee.account.accounts.domain.Account;
import com.hglee.account.accounts.domain.repository.IAccountRepository;
import com.hglee.account.accounts.dto.AccountResponseDto;
import com.hglee.account.accounts.exception.NotFoundException;

@Service
public class SignInWithEmailService implements SignInWithEmailUseCase {

	private final IAccountRepository repository;
	private final PasswordEncoder passwordEncoder;

	public SignInWithEmailService(IAccountRepository repository, PasswordEncoder passwordEncoder) {
		this.repository = repository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public AccountResponseDto execute(SignInWithEmailCommand command) {
		Account foundAccount = this.repository.findByEmail(command.getEmail())
				.orElseThrow(() -> new NotFoundException("가입된 계정을 찾을 수 없습니다."));

		if (!foundAccount.isSignedUp()) {
			throw new IllegalArgumentException("로그인 가능한 계정이 아닙니다.");
		}

		boolean passwordMatches = passwordEncoder.matches(command.getPassword(), foundAccount.getPassword());

		if (!passwordMatches) {
			throw new IllegalArgumentException("올바른 패스워드가 아닙니다.");
		}

		return new AccountResponseDto(foundAccount.getId(), foundAccount.getMobile(), foundAccount.getStatusAsString(),
				foundAccount.getEmail(), foundAccount.getName(), foundAccount.getNickName());
	}
}
