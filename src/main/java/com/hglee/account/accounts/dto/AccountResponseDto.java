package com.hglee.account.accounts.dto;

public class AccountResponseDto {
	private final String id;
	private final String mobile;
	private final String status;
	private final String email;
	private final String name;
	private final String nickName;

	public AccountResponseDto(String id, String mobile, String status, String email, String name, String nickName) {
		this.id = id;
		this.mobile = mobile;
		this.status = status;
		this.email = email;
		this.name = name;
		this.nickName = nickName;
	}

	public String getId() {
		return id;
	}

	public String getMobile() {
		return mobile;
	}

	public String getStatus() {
		return status;
	}

	public String getEmail() {
		return email;
	}

	public String getName() {
		return name;
	}

	public String getNickName() {
		return nickName;
	}
}
