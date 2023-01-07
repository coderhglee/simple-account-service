package com.hglee.account.accounts.application.command;

import static org.assertj.core.api.Assertions.*;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class VerifyAccountCommandTest {
	@DisplayName("mobile이 올바르지 않은 경우, ConstraintViolationException이 발생한다.")
	@ParameterizedTest
	@ValueSource(strings = {"", " ", "010-1234-1234", "+8210-1234-1234", "821023451234"})
	void invalid_mobile(String input) {
		assertThatThrownBy(() -> new VerifyAccountCommand(input, "123456")).isInstanceOf(
				ConstraintViolationException.class).hasMessageMatching("mobile: (?s).*");
	}

	@DisplayName("code가 올바르지 않은 경우, ConstraintViolationException이 발생한다.")
	@ParameterizedTest
	@ValueSource(strings = {"", "1234", "abc", " "})
	void invalid_code(String input) {
		assertThatThrownBy(() -> new VerifyAccountCommand("01012341234", input)).isInstanceOf(
				ConstraintViolationException.class).hasMessageMatching("code: (?s).*");
	}

}