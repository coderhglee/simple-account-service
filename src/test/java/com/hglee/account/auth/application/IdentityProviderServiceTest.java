package com.hglee.account.auth.application;

import static org.assertj.core.api.BDDAssertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.hglee.account.accounts.domain.Account;
import com.hglee.account.accounts.domain.repository.IAccountRepository;
import com.hglee.account.accounts.exception.NotFoundException;
import com.hglee.account.accounts.factory.AccountFactory;
import com.hglee.account.auth.dto.AuthenticationResponse;
import com.hglee.account.auth.infrastructure.client.IAccountManagementClient;
import com.hglee.account.auth.infrastructure.security.TokenProvider;

@SpringBootTest
class IdentityProviderServiceTest {
	@Autowired
	IAccountManagementClient accountManagementClient;

	@Autowired
	InteractionProvider interactionProvider;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	IAccountRepository accountRepository;

	@Autowired
	TokenProvider tokenProvider;

	IdentityProvider identityProvider;

	@BeforeEach
	void before() {
		identityProvider = new IdentityProviderService(accountManagementClient, interactionProvider, tokenProvider);
	}

	@Nested
	@DisplayName("signInWithMobile")
	class sign_in_with_mobile {
		@Nested
		@DisplayName("모바일로 가입된 계정이 존재하면")
		class exist_signed_up_mobile {
			String mobile;
			String password;

			@BeforeEach
			void before() {
				AccountFactory factory = AccountFactory.build();

				mobile = factory.getMobile();
				password = factory.getPassword();

				회원가입_완료_됨(mobile, password);
			}

			@DisplayName("엑세스 토큰을 발급 받을 수 있다.")
			@Test
			void issued_access_token() {
				AuthenticationResponse authenticationResponse = identityProvider.signInWithMobile(mobile, password);

				then(authenticationResponse.getAccessToken()).isExactlyInstanceOf(String.class);
				then(authenticationResponse.getIssuedAt()).isBefore(authenticationResponse.getExpiresAt());
			}
		}

		@Nested
		@DisplayName("모바일로 가입된 계정이 존재하면")
		class not_exist_mobile_account {
			@DisplayName("엑세스 토큰을 발급 받을 수 있다.")
			@Test
			void issued_access_token() {
				AccountFactory accountFactory = AccountFactory.build();

				assertThatThrownBy(() -> identityProvider.signInWithMobile(accountFactory.getMobile(),
						accountFactory.getPassword())).isInstanceOf(NotFoundException.class)
						.hasMessageContaining("가입된 계정을 찾을 수 없습니다.");
			}
		}
	}

	private void 회원가입_완료_됨(String mobile, String password) {
		Account signedUpAccount = AccountFactory.isSignedUpAccount(mobile, encoder.encode(password));

		accountRepository.save((signedUpAccount));
	}
}