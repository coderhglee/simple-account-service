package com.hglee.account.auth.dto;

import java.time.LocalDateTime;

public class CreateOauth2TokenDto {
	private LocalDateTime expiresAt;

	private LocalDateTime issuedAt;

	private String subject;

	public CreateOauth2TokenDto(LocalDateTime expiresAt, LocalDateTime issuedAt, String subject) {
		this.expiresAt = expiresAt;
		this.issuedAt = issuedAt;
		this.subject = subject;
	}

	public LocalDateTime getExpiresAt() {
		return expiresAt;
	}

	public String getSubject() {
		return subject;
	}

	public LocalDateTime getIssuedAt() {
		return issuedAt;
	}
}
