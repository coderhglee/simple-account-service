package com.hglee.account.accounts.application.service;

import org.springframework.stereotype.Service;

import com.hglee.account.accounts.application.command.SignInWithMobileCommand;
import com.hglee.account.accounts.application.usecase.FindSignedUpAccountUseCase;
import com.hglee.account.accounts.application.usecase.IAccountService;
import com.hglee.account.accounts.application.usecase.SignInWithMobileUseCase;
import com.hglee.account.accounts.dto.AccountResponseDto;

@Service
public class AccountService implements IAccountService {
	private final SignInWithMobileUseCase signInWithMobileUseCase;
	private final FindSignedUpAccountUseCase findSignedUpAccountUseCase;

	public AccountService(SignInWithMobileUseCase signInWithMobileUseCase,
			FindSignedUpAccountUseCase findSignedUpAccountUseCase) {
		this.signInWithMobileUseCase = signInWithMobileUseCase;
		this.findSignedUpAccountUseCase = findSignedUpAccountUseCase;
	}

	@Override
	public AccountResponseDto signInWithMobile(String mobile, String password) {
		return this.signInWithMobileUseCase.execute(new SignInWithMobileCommand(mobile, password));
	}

	@Override
	public AccountResponseDto findById(String id) {
		return findSignedUpAccountUseCase.execute(id);
	}
}
