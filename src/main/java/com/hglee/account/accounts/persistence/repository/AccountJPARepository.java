package com.hglee.account.accounts.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hglee.account.accounts.domain.Account;
import com.hglee.account.accounts.domain.Status;

public interface AccountJPARepository extends JpaRepository<Account, String> {
	Optional<Account> findByMobile(String mobile);
	Optional<Account> findByMobileAndStatus(String mobile, Status status);
}
