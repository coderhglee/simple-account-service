package com.hglee.account.auth.dto;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AccessTokenEncodedDto {
	private Instant iss;
	private Instant exp;
	private String accessToken;
}
