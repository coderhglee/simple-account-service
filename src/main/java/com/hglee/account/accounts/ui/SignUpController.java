package com.hglee.account.accounts.ui;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hglee.account.accounts.application.command.RequestAccountVerificationCommand;
import com.hglee.account.accounts.application.command.VerifyAccountCommand;
import com.hglee.account.accounts.application.usecase.RequestAccountVerificationUseCase;
import com.hglee.account.accounts.application.usecase.VerifyAccountUseCase;
import com.hglee.account.accounts.dto.RequestAccountVerificationMobileRequest;
import com.hglee.account.accounts.dto.VerifyMobileRequest;

@RestController
public class SignUpController {

	private final RequestAccountVerificationUseCase requestAccountVerificationUseCase;
	private final VerifyAccountUseCase verifyAccountUseCase;

	public SignUpController(RequestAccountVerificationUseCase requestAccountVerificationUseCase,
			VerifyAccountUseCase verifyAccountUseCase) {
		this.requestAccountVerificationUseCase = requestAccountVerificationUseCase;
		this.verifyAccountUseCase = verifyAccountUseCase;
	}

	@PostMapping(value = "/sign-up/request-account-verification-mobile", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> requestAccountVerificationMobile(
			@RequestBody final RequestAccountVerificationMobileRequest request) {

		requestAccountVerificationUseCase.execute(new RequestAccountVerificationCommand(request.getMobile()));

		return ResponseEntity.ok().build();
	}

	@PostMapping(value = "/sign-up/verify-mobile", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> verifyMobile(@RequestBody final VerifyMobileRequest request) {
		verifyAccountUseCase.execute(new VerifyAccountCommand(request.getMobile(), request.getCode()));

		return ResponseEntity.ok().build();
	}

}
