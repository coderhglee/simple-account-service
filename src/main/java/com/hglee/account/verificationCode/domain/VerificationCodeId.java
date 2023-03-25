package com.hglee.account.verificationCode.domain;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class VerificationCodeId implements Serializable {
	private String identifier;
	private String code;

	public static VerificationCodeId of(String identifier, String code) {
		return new VerificationCodeId(identifier, code);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		VerificationCodeId verificationCodeId = (VerificationCodeId)o;
		return Objects.equals(identifier, verificationCodeId.identifier) && Objects.equals(code, verificationCodeId.code);
	}

	@Override
	public int hashCode() {
		return Objects.hash(identifier, code);
	}
}
