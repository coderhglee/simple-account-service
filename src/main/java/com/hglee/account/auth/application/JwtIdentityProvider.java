package com.hglee.account.auth.application;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.stereotype.Service;

import com.hglee.account.accounts.application.usecase.IAccountService;
import com.hglee.account.accounts.dto.AccountResponseDto;
import com.hglee.account.auth.dto.CreateOauth2TokenDto;
import com.hglee.account.auth.dto.AuthenticationResponse;

@Service
public class JwtIdentityProvider implements IdentityProvider {
	private final IAccountService accountService;
	private final TokenProvider tokenProvider;

	public JwtIdentityProvider(IAccountService accountService, TokenProvider tokenProvider) {
		this.accountService = accountService;
		this.tokenProvider = tokenProvider;
	}

	@Override
	public AuthenticationResponse signInWithMobile(String mobile, String password) {
		AccountResponseDto accountResponseDto = this.accountService.signInWithMobile(mobile, password);

		LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);

		OAuth2Token oAuth2Token = tokenProvider.encode(
				new CreateOauth2TokenDto(now.plusMinutes(5L),
						now, accountResponseDto.getId()));

		return AuthenticationResponse.of(oAuth2Token.getTokenValue(), oAuth2Token.getIssuedAt(),
				oAuth2Token.getExpiresAt());
	}
}
