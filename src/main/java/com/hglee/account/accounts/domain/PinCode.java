package com.hglee.account.accounts.domain;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.stream.IntStream;

public class PinCode {
	private String code;

	private LocalDateTime createdAt;

	private LocalDateTime expiresAt;

	protected PinCode() {
	}

	public PinCode(String code, LocalDateTime createdAt, LocalDateTime expiresAt) {
		this.code = code;
		this.createdAt = createdAt;
		this.expiresAt = expiresAt;
	}

	public boolean isSameCode(String code) {
		return this.code.equals(code);
	}

	public boolean isExpired() {
		return this.expiresAt.isBefore(LocalDateTime.now());
	}

	public String getCode() {
		return code;
	}

	public LocalDateTime getExpiresAt() {
		return expiresAt;
	}

	public static PinCode generateCode() {
		Random random = new Random();

		String code = IntStream.rangeClosed(1, 6)
				.collect(StringBuilder::new, (builder, number) -> builder.append(random.nextInt(9)),
						StringBuilder::append)
				.toString();

		return new PinCode(code, LocalDateTime.now(), LocalDateTime.now().plusMinutes(3L));
	}
}
