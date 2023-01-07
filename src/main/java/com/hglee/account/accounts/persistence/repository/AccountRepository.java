package com.hglee.account.accounts.persistence.repository;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.hglee.account.accounts.domain.Account;
import com.hglee.account.accounts.domain.repository.IAccountRepository;

@Component
public class AccountRepository implements IAccountRepository {

	private final AccountJPARepository repository;

	public AccountRepository(AccountJPARepository repository) {
		this.repository = repository;
	}

	@Override
	public Account save(Account account) {
		return this.repository.save(account);
	}

	@Override
	public Optional<Account> findByMobile(String mobile) {
		return this.repository.findByMobile(mobile);
	}
}
