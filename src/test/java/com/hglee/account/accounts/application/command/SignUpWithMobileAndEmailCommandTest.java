package com.hglee.account.accounts.application.command;

import static org.assertj.core.api.Assertions.*;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class SignUpWithMobileAndEmailCommandTest {

	@DisplayName("유효한 회원정보를 입력한 경우 command가 생성된다.")
	@Test
	void valid_command() {
		SignUpWithMobileAndEmailCommand signUpCommand = new SignUpWithMobileAndEmailCommand("01012341234",
				"test@naver.com", "abcd1234!", "홍길동", "홍길동님", "123456");

		assertThat(signUpCommand).isNotNull();
	}

	@DisplayName("email이 올바르지 않은 경우, ConstraintViolationException이 발생한다.")
	@ParameterizedTest
	@ValueSource(strings = {"", " ", "*abcd1"})
	void invalid_email(String input) {
		assertThatThrownBy(() -> new SignUpWithMobileAndEmailCommand("01012341234", input, "abcd1234!", "홍길동", "홍길동님",
				"123456")).isInstanceOf(ConstraintViolationException.class).hasMessageMatching("email: (?s).*");
	}

	@DisplayName("mobile이 올바르지 않은 경우, ConstraintViolationException이 발생한다.")
	@ParameterizedTest
	@ValueSource(strings = {"", " ", "010-1234-1234", "+8210-1234-1234", "821023451234"})
	void invalid_mobile(String input) {
		assertThatThrownBy(() -> new SignUpWithMobileAndEmailCommand(input, "test@test.com", "abcd1234!", "홍길동", "홍길동님",
				"123456")).isInstanceOf(ConstraintViolationException.class).hasMessageMatching("mobile: (?s).*");
	}

	@DisplayName("password가 올바르지 않은 경우, ConstraintViolationException이 발생한다.")
	@ParameterizedTest
	@ValueSource(strings = {"", " ", "<script>"})
	void invalid_password(String input) {
		assertThatThrownBy(
				() -> new SignUpWithMobileAndEmailCommand("01012341234", "test@naver.com", input, "홍길동", "홍길동님",
						"123456")).isInstanceOf(ConstraintViolationException.class)
				.hasMessageMatching("password: (?s).*");
	}

	@DisplayName("name이 올바르지 않은 경우, ConstraintViolationException이 발생한다.")
	@ParameterizedTest
	@ValueSource(strings = {"", " ", "name!", "name1234"})
	void invalid_name(String input) {
		assertThatThrownBy(
				() -> new SignUpWithMobileAndEmailCommand("01012341234", "test@naver.com", "abcd1234!", input, "홍길동님",
						"123456")).isInstanceOf(ConstraintViolationException.class).hasMessageMatching("name: (?s).*");
	}

	@DisplayName("nickname이 올바르지 않은 경우, ConstraintViolationException이 발생한다.")
	@ParameterizedTest
	@ValueSource(strings = {"", " ", "name!", "name1234*"})
	void invalid_nickName(String input) {
		assertThatThrownBy(
				() -> new SignUpWithMobileAndEmailCommand("01012341234", "test@naver.com", "abcd1234!", "valid", input,
						"123456")).isInstanceOf(ConstraintViolationException.class)
				.hasMessageMatching("nickName: (?s).*");
	}
}