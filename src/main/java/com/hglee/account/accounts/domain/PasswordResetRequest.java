package com.hglee.account.accounts.domain;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.stream.IntStream;

import javax.persistence.Column;

public class PasswordResetRequest {
	private String code;

	@Column(nullable = true)
	private boolean isConfirmed = false;

	private LocalDateTime createdAt;

	private LocalDateTime expiresAt;

	public String getCode() {
		return code;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getExpiresAt() {
		return expiresAt;
	}

	public void confirm() {
		this.isConfirmed = true;
	}

	public PasswordResetRequest() {
	}

	public boolean isSameCode(String code) {
		return this.code.equals(code);
	}

	public boolean isExpired() {
		return this.expiresAt.isBefore(LocalDateTime.now());
	}

	public PasswordResetRequest(String code, boolean isConfirmed, LocalDateTime createdAt, LocalDateTime expiresAt) {
		this.code = code;
		this.isConfirmed = isConfirmed;
		this.createdAt = createdAt;
		this.expiresAt = expiresAt;
	}

	public static PasswordResetRequest generate() {
		Random random = new Random();

		String code = IntStream.rangeClosed(1, 6)
				.collect(StringBuilder::new, (builder, number) -> builder.append(random.nextInt(9)),
						StringBuilder::append)
				.toString();

		return new PasswordResetRequest(code, false, LocalDateTime.now(), LocalDateTime.now().plusMinutes(3L));
	}

	public boolean isConfirmed() {
		return this.isConfirmed;
	}
}
