package com.hglee.account.common.ui;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.hglee.account.accounts.exception.ConflictException;
import com.hglee.account.accounts.exception.NotFoundException;
import com.hglee.account.common.dto.ErrorResponse;

@RestControllerAdvice
public class ExceptionController {
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException exception) {
		final ErrorResponse response = ErrorResponse.of(exception.getMessage(),
				HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase(), HttpStatus.UNPROCESSABLE_ENTITY.value(),
				exception.getConstraintViolations());

		return ResponseEntity.unprocessableEntity().body(response);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> handleForbiddenException(IllegalArgumentException exception) {
		final ErrorResponse response = ErrorResponse.of(exception.getMessage(), HttpStatus.FORBIDDEN.getReasonPhrase(),
				HttpStatus.FORBIDDEN.value());

		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
	}

	@ExceptionHandler(ConflictException.class)
	public ResponseEntity<ErrorResponse> handleConflictException(ConflictException exception) {
		final ErrorResponse response = ErrorResponse.of(exception.getMessage(), HttpStatus.CONFLICT.getReasonPhrase(),
				HttpStatus.CONFLICT.value());

		return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
	}

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException exception) {
		final ErrorResponse response = ErrorResponse.of(exception.getMessage(), HttpStatus.NOT_FOUND.getReasonPhrase(),
				HttpStatus.NOT_FOUND.value());

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	}
}
