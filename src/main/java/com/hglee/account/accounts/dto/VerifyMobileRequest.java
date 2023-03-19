package com.hglee.account.accounts.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class VerifyMobileRequest {
	@ApiModelProperty(value = "전화번호", required = true, notes = "숫자", example = "01012341234")
	private String mobile;
	@ApiModelProperty(value = "인증코드", required = true, notes = "숫자 6자리", example = "123456")
	private String code;
	@ApiModelProperty(value = "interactionId", required = true)
	private String interactionId;
}
