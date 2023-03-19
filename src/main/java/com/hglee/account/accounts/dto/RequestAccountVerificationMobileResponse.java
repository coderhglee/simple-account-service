package com.hglee.account.accounts.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RequestAccountVerificationMobileResponse {
	private String mobile;
	private String interactionId;
}
