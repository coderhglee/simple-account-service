package com.hglee.account.auth.dto;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class AuthenticationResponse {
	private String accessToken;
	private LocalDateTime issuedAt;
	private LocalDateTime expiresAt;

	public AuthenticationResponse() {
	}

	public AuthenticationResponse(String accessToken, LocalDateTime issuedAt, LocalDateTime expiresAt) {
		this.accessToken = accessToken;
		this.issuedAt = issuedAt;
		this.expiresAt = expiresAt;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public LocalDateTime getIssuedAt() {
		return issuedAt;
	}

	public LocalDateTime getExpiresAt() {
		return expiresAt;
	}

	public static AuthenticationResponse of(String accessToken, Instant issuedAt, Instant expiresAt) {
		return new AuthenticationResponse(accessToken, LocalDateTime.ofInstant(issuedAt, ZoneOffset.UTC),
				LocalDateTime.ofInstant(expiresAt, ZoneOffset.UTC));
	}
}
