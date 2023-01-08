package com.hglee.account.accounts.dto;

import io.swagger.annotations.ApiModelProperty;

public class VerifyMobileRequest {
	@ApiModelProperty(value = "전화번호", required = true, notes = "숫자", example = "01012341234")
	private String mobile;
	@ApiModelProperty(value = "인증코드", required = true, notes = "숫자 6자리", example = "123456")
	private String code;

	public VerifyMobileRequest() {
	}

	public VerifyMobileRequest(String mobile, String code) {
		this.mobile = mobile;
		this.code = code;
	}

	public String getMobile() {
		return mobile;
	}

	public String getCode() {
		return code;
	}
}
