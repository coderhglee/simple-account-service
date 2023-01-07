package com.hglee.account.common.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;

public class ErrorResponse {

	private final String message;
	private final String code;
	private final int status;
	private final List<String> errors;

	protected ErrorResponse(String message, String code, int status, List<String> errors) {
		this.message = message;
		this.code = code;
		this.status = status;
		this.errors = errors;
	}

	public static ErrorResponse of(String message, String code, int status) {
		return new ErrorResponse(message, code, status, new ArrayList<>());
	}

	public static ErrorResponse of(String message, String code, int status, Set<ConstraintViolation<?>> errors) {
		return new ErrorResponse(message, code, status,
				errors.stream().map((ConstraintViolation::getMessage)).collect(Collectors.toList()));
	}

	public String getMessage() {
		return message;
	}

	public int getStatus() {
		return status;
	}

	public List<String> getErrors() {
		return errors;
	}

	public String getCode() {
		return code;
	}
}