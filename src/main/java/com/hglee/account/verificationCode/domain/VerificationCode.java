package com.hglee.account.verificationCode.domain;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.stream.IntStream;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
public class VerificationCode {
	@EmbeddedId
	private VerificationCodeId id;

	private LocalDateTime createdAt;

	private LocalDateTime expiresAt;

	@Column(nullable = true)
	private boolean isConfirmed = false;

	public VerificationCode(VerificationCodeId id, LocalDateTime createdAt, LocalDateTime expiresAt) {
		this.id = id;
		this.createdAt = createdAt;
		this.expiresAt = expiresAt;
	}

	public boolean isExpired() {
		return this.expiresAt.isBefore(LocalDateTime.now()) || this.isConfirmed;
	}

	public boolean isConfirmed() {
		return this.isConfirmed;
	}

	public String getCode() {
		return this.id.getCode();
	}

	public LocalDateTime getExpiresAt() {
		return expiresAt;
	}

	public static VerificationCode generate(String mobile) {
		String code = generateRandomCode();

		return new VerificationCode(new VerificationCodeId(mobile, code), LocalDateTime.now(),
				LocalDateTime.now().plusMinutes(3L));
	}

	private static String generateRandomCode() {
		Random random = new Random();

		return IntStream.rangeClosed(1, 6)
				.collect(StringBuilder::new, (builder, number) -> builder.append(random.nextInt(9)),
						StringBuilder::append)
				.toString();
	}

	public void verify() {
		this.isConfirmed = true;
	}
}
