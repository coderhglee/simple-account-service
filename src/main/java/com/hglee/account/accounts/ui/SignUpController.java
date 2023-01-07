package com.hglee.account.accounts.ui;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hglee.account.accounts.application.command.RequestAccountVerificationCommand;
import com.hglee.account.accounts.application.usecase.RequestAccountVerificationUseCase;
import com.hglee.account.accounts.dto.RequestAccountVerificationMobileRequest;

@RestController
public class SignUpController {

	private final RequestAccountVerificationUseCase requestAccountVerificationUseCase;

	public SignUpController(RequestAccountVerificationUseCase requestAccountVerificationUseCase) {
		this.requestAccountVerificationUseCase = requestAccountVerificationUseCase;
	}

	@PostMapping(value = "/sign-up/request-account-verification-mobile", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> requestAccountVerificationMobile(
			@RequestBody final RequestAccountVerificationMobileRequest request) {

		requestAccountVerificationUseCase.execute(new RequestAccountVerificationCommand(request.getMobile()));

		return ResponseEntity.ok().build();
	}
}
