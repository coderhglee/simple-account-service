package com.hglee.account.accounts.dto;

import io.swagger.annotations.ApiModelProperty;

public class SignUpMobileRequest {
	@ApiModelProperty(value = "전화번호", required = true, notes = "숫자", example = "01012341234")
	private String mobile;
	@ApiModelProperty(value = "이메일", required = true, example = "test@email.com")
	private String email;
	@ApiModelProperty(value = "비밀번호", required = true, notes = "최소 6글자 최대 18글자 영문(대소문자), 숫자, 특수문자를 허용합니다.", example = "password1234!")
	private String password;
	@ApiModelProperty(value = "이름", required = true, notes = "최소 1글자 최대 10글자 영문, 한글", example = "SomeName")
	private String name;
	@ApiModelProperty(value = "닉네임", required = true, notes = "최소 1글자 최대 20글자 영문, 한글만 허용", example = "someNickName")
	private String nickName;

	public SignUpMobileRequest() {
	}

	public SignUpMobileRequest(String mobile, String email, String password, String name, String nickName) {
		this.mobile = mobile;
		this.email = email;
		this.password = password;
		this.name = name;
		this.nickName = nickName;
	}

	public String getMobile() {
		return mobile;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public String getName() {
		return name;
	}

	public String getNickName() {
		return nickName;
	}
}
