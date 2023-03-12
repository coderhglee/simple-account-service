package com.hglee.account.accounts.application.service;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.BDDAssertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.hglee.account.accounts.application.usecase.FindSignedUpAccountUseCase;
import com.hglee.account.accounts.application.usecase.SignUpWithMobileAndEmailUseCase;
import com.hglee.account.accounts.domain.Account;
import com.hglee.account.accounts.domain.Status;
import com.hglee.account.accounts.domain.repository.IAccountRepository;
import com.hglee.account.accounts.dto.AccountResponseDto;
import com.hglee.account.accounts.exception.NotFoundException;
import com.hglee.account.accounts.factory.AccountFactory;

@SpringBootTest
class FindSignedUpAccountServiceTest {
	@Autowired
	IAccountRepository repository;

	@Autowired
	SignUpWithMobileAndEmailUseCase signUpWithMobileAndEmailUseCase;

	FindSignedUpAccountUseCase findSignedUpAccountUseCase;

	@BeforeEach
	void before() {
		findSignedUpAccountUseCase = new FindSignedUpAccountService(repository);
	}

	@Nested
	@DisplayName("Context: 전화번호로 가입된 계정을 조회하는 경우")
	class signed_up_mobile {
		String id;

		@BeforeEach
		void before() {
			Account signedUpAccount = AccountFactory.isSignedUpAccount();

			repository.save(signedUpAccount);

			id = signedUpAccount.getId();
		}

		@DisplayName("가입된 계정을 id로 조회할 수 있다")
		@Test
		void success_get_account() {
			AccountResponseDto accountResponseDto = findSignedUpAccountUseCase.execute(id);

			then(accountResponseDto.getId()).isEqualTo(id);
			then(accountResponseDto.getStatus()).isEqualTo(Status.ACTIVATED.name());
		}
	}

	@Nested
	@DisplayName("Context: 계정의 상태가 activated가 아닌 계정을 조회하는 경우")
	class not_activated_signed_up_mobile {
		String id;

		@BeforeEach
		void before() {
			AccountFactory accountFactory = AccountFactory.build();

			Account signedUpAccount = new Account(accountFactory.getId(), accountFactory.getMobile(),
					accountFactory.getEmail(), Status.DELETED, accountFactory.getName(),
					accountFactory.getNickName());

			repository.save(signedUpAccount);

			id = signedUpAccount.getId();
		}

		@DisplayName("가입된 계정을 id로 조회할 수 있다")
		@Test
		void success_get_account() {
			assertThatThrownBy(() -> findSignedUpAccountUseCase.execute(id)).isInstanceOf(
					IllegalArgumentException.class).hasMessageContaining("로그인 가능한 계정이 아닙니다.");
		}
	}

	@Nested
	@DisplayName("Context: 존재하지 않는 계정을 조회하는 경우")
	class not_exist_signed_up_mobile {
		@DisplayName("에러가 발생한다.")
		@Test
		void success_get_account() {
			AccountFactory accountFactory = AccountFactory.build();

			assertThatThrownBy(() -> findSignedUpAccountUseCase.execute(accountFactory.getId())).isInstanceOf(
					NotFoundException.class).hasMessageContaining("가입된 계정을 찾을 수 없습니다.");
		}
	}
}