package com.hglee.account.accounts.factory;

import java.time.LocalDateTime;

import com.github.javafaker.Faker;

public class PinCodeFactory {
	private String code;

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

	public PinCodeFactory(String code, LocalDateTime createdAt, LocalDateTime expiresAt) {
		this.code = code;
		this.createdAt = createdAt;
		this.expiresAt = expiresAt;
	}

	public static PinCodeFactory build() {
		Faker faker = Faker.instance();
		return new PinCodeFactory(faker.random().nextInt(100000, 999999).toString(), LocalDateTime.now(),
				LocalDateTime.now().plusMinutes(3));
	}
}
