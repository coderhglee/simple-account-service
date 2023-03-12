package com.hglee.account.verificationCode.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateVerificationCodeResponse {
	String code;

	public static CreateVerificationCodeResponse of(String code) {
		return new CreateVerificationCodeResponse(code);
	}
}
