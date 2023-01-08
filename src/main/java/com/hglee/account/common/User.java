package com.hglee.account.common;

import io.swagger.annotations.ApiModelProperty;

public class User {
	@ApiModelProperty(value = "유저 고유 아이디")
	private String id;

	@ApiModelProperty(value = "status")
	private String status;

	@ApiModelProperty(value = "email")
	private String email;

	@ApiModelProperty(value = "mobile")
	private String mobile;

	@ApiModelProperty(value = "name")
	private String name;

	@ApiModelProperty(value = "nickName")
	private String nickName;

	public User() {
	}

	public User(String id, String status, String email, String mobile, String name, String nickName) {
		this.id = id;
		this.status = status;
		this.email = email;
		this.mobile = mobile;
		this.name = name;
		this.nickName = nickName;
	}

	public String getId() {
		return id;
	}

	public String getStatus() {
		return status;
	}

	public String getEmail() {
		return email;
	}

	public String getMobile() {
		return mobile;
	}

	public String getName() {
		return name;
	}

	public String getNickName() {
		return nickName;
	}
}
