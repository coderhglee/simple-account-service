package com.hglee.account.accounts.domain.repository;

import java.util.Optional;

import com.hglee.account.accounts.domain.Account;
import com.hglee.account.accounts.domain.Status;

public interface IAccountRepository {
	Account save(Account account);

	Optional<Account> findByMobile(String mobile);

	Optional<Account> findByMobileAndStatus(String mobile, Status status);
}
