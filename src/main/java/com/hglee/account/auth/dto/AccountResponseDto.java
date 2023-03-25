package com.hglee.account.auth.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
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

	public static AccountResponseDto of(String id, String mobile, String status, String email, String name,
			String nickName) {
		return new AccountResponseDto(id, mobile, status, email, name, nickName);
	}
}
