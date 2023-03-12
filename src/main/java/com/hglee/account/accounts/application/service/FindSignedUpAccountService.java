package com.hglee.account.accounts.application.service;

import org.springframework.stereotype.Service;

import com.hglee.account.accounts.application.usecase.FindSignedUpAccountUseCase;
import com.hglee.account.accounts.domain.Account;
import com.hglee.account.accounts.domain.repository.IAccountRepository;
import com.hglee.account.accounts.dto.AccountResponseDto;
import com.hglee.account.accounts.exception.NotFoundException;

@Service
public class FindSignedUpAccountService implements FindSignedUpAccountUseCase {

	private final IAccountRepository repository;

	public FindSignedUpAccountService(IAccountRepository repository) {
		this.repository = repository;
	}

	@Override
	public AccountResponseDto execute(String id) {
		Account foundAccount = this.repository.findById(id)
				.orElseThrow(() -> new NotFoundException("가입된 계정을 찾을 수 없습니다."));

		if (!foundAccount.isSignedUp()) {
			throw new IllegalArgumentException("로그인 가능한 계정이 아닙니다.");
		}

		return new AccountResponseDto(foundAccount.getId(), foundAccount.getMobile(), foundAccount.getStatusAsString(),
				foundAccount.getEmail(), foundAccount.getName(), foundAccount.getNickName());
	}
}
