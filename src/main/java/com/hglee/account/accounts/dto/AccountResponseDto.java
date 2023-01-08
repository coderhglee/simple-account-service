package com.hglee.account.accounts.dto;

import io.swagger.annotations.ApiModelProperty;

public class AccountResponseDto {
	@ApiModelProperty(value = "아이디", required = true)
	private final String id;
	@ApiModelProperty(value = "전화번호", required = true)
	private final String mobile;
	@ApiModelProperty(value = "계정의 상태", required = true)
	private final String status;
	@ApiModelProperty(value = "이메일", required = true)
	private final String email;
	@ApiModelProperty(value = "이름", required = true)
	private final String name;
	@ApiModelProperty(value = "닉네임", required = true)
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
