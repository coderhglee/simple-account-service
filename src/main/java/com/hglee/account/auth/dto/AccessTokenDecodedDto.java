package com.hglee.account.auth.dto;

import java.time.Instant;

public class AccessTokenDecodedDto {
	private Instant iss;
	private Instant exp;
	private String sub;

	public AccessTokenDecodedDto(Instant iss, Instant exp, String sub) {
		this.iss = iss;
		this.exp = exp;
		this.sub = sub;
	}

	public Instant getIss() {
		return iss;
	}

	public Instant getExp() {
		return exp;
	}

	public String getSub() {
		return sub;
	}
}
