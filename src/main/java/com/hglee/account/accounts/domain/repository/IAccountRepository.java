package com.hglee.account.accounts.domain.repository;

import java.util.Optional;

import com.hglee.account.accounts.domain.Account;

public interface IAccountRepository {
	Account save(Account account);

	Optional<Account> findByMobile(String mobile);
}
