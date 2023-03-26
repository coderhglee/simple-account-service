package com.hglee.account.auth.application;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.stereotype.Service;

import com.hglee.account.auth.dto.AccessTokenEncodedDto;
import com.hglee.account.auth.dto.AccountResponseDto;
import com.hglee.account.auth.dto.CreateOauth2TokenDto;
import com.hglee.account.auth.dto.AuthenticationResponse;
import com.hglee.account.auth.dto.InteractionResponse;
import com.hglee.account.auth.dto.RequestAccountVerificationMobileResponse;
import com.hglee.account.auth.dto.SignUpMobileRequest;
import com.hglee.account.auth.infrastructure.client.IAccountManagementClient;
import com.hglee.account.auth.infrastructure.security.TokenProvider;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class IdentityProviderService implements IdentityProvider {
	private static final String SIGN_UP_PROMPT = "sign-up";

	private final IAccountManagementClient accountManagementClient;
	private final InteractionProvider interactionProvider;
	private final TokenProvider tokenProvider;

	@Override
	public AuthenticationResponse signInWithMobile(String mobile, String password) {
		AccountResponseDto accountResponseDto = this.accountManagementClient.signInWithMobile(mobile, password);

		return grantAuthentication(accountResponseDto);
	}

	@Override
	public AuthenticationResponse signInWithEmail(String email, String password) {
		AccountResponseDto accountResponseDto = this.accountManagementClient.signInWithEmail(email, password);

		return grantAuthentication(accountResponseDto);
	}

	@Override
	public RequestAccountVerificationMobileResponse requestAccountVerificationMobile(String mobile) {
		InteractionResponse interactionResponse = this.interactionProvider.create(SIGN_UP_PROMPT);

		this.accountManagementClient.requestAccountVerificationMobile(mobile);

		return new RequestAccountVerificationMobileResponse(mobile, interactionResponse.getInteractionId());
	}

	@Override
	public void verifyMobile(String mobile, String code, String interactionId) {
		this.interactionProvider.verify(interactionId, SIGN_UP_PROMPT);

		this.accountManagementClient.verifyMobile(mobile, code);
	}

	@Override
	public AuthenticationResponse signUpWithMobile(SignUpMobileRequest request) {
		this.interactionProvider.verify(request.getInteractionId(), SIGN_UP_PROMPT);

		AccountResponseDto accountResponseDto = this.accountManagementClient.signUpWithMobile(request);

		this.interactionProvider.finish(request.getInteractionId());

		return grantAuthentication(accountResponseDto);
	}

	private AuthenticationResponse grantAuthentication(AccountResponseDto accountResponseDto) {
		LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);

		AccessTokenEncodedDto encodeToken = this.tokenProvider.encode(
				new CreateOauth2TokenDto(now.plusMinutes(5L), now, accountResponseDto.getId()));

		return AuthenticationResponse.of(encodeToken.getAccessToken(), encodeToken.getIss(), encodeToken.getExp());
	}
}
