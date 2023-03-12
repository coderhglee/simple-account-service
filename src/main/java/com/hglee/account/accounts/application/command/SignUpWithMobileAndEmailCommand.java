package com.hglee.account.accounts.application.command;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.hglee.account.core.SelfValidator;

public class SignUpWithMobileAndEmailCommand extends SelfValidator<SignUpWithMobileAndEmailCommand> {
	@NotNull
	@Pattern(regexp = "^\\d{3}(\\d{4})\\d{4}$", message = "전화번호 형식이 올바르지 않습니다.")
	private final String mobile;
	@NotNull
	@Pattern(regexp = "^([\\S\\d]+)@(\\S+)[.](\\S+.?\\S+)$", message = "이메일 형식이 올바르지 않습니다.")
	private final String email;
	@NotNull
	@Pattern(regexp = "^[A-Za-z0-9\\w~@#$%^&*+=`|{}:;!.?\\\"()\\[\\]-]{6,18}$", message = "패스워드 형식이 올바르지 않습니다. 최소 6글자 최대 18글자 영문(대소문자), 숫자, 특수문자를 허용합니다.")
	private final String password;
	@NotNull
	@Pattern(regexp = "^(?!\\s*$)[ㄱ-ㅎ|가-힣|a-z|A-Z]{1,10}$", message = "이름 형식이 올바르지 않습니다. 영문, 한글만 허용합니다. 최소 1글자 최대 10글자")
	private final String name;
	@NotNull
	@Pattern(regexp = "^(?!\\s*$)[ㄱ-ㅎ|가-힣|a-z|A-Z|0-9]{1,20}$", message = "이름 형식이 올바르지 않습니다. 영문, 한글만 허용합니다.")
	private final String nickName;

	@NotNull
	@Pattern(regexp = "^[0-9]{6}$", message = "인증코드 형식이 올바르지 않습니다.")
	private final String code;

	public SignUpWithMobileAndEmailCommand(String mobile, String email, String password, String name, String nickName,
			String code) {
		this.mobile = mobile;
		this.email = email;
		this.password = password;
		this.name = name;
		this.nickName = nickName;
		this.code = code;

		this.validateSelf();
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

	public String getCode() {
		return code;
	}
}
