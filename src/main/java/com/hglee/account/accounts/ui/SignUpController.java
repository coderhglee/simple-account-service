package com.hglee.account.accounts.ui;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hglee.account.accounts.application.command.RequestAccountVerificationCommand;
import com.hglee.account.accounts.application.command.SignUpWithMobileAndEmailCommand;
import com.hglee.account.accounts.application.command.VerifyAccountCommand;
import com.hglee.account.accounts.application.usecase.RequestAccountVerificationUseCase;
import com.hglee.account.accounts.application.usecase.SignUpWithMobileAndEmailUseCase;
import com.hglee.account.accounts.application.usecase.VerifyAccountUseCase;
import com.hglee.account.accounts.dto.AccountResponseDto;
import com.hglee.account.accounts.dto.RequestAccountVerificationMobileRequest;
import com.hglee.account.accounts.dto.RequestAccountVerificationMobileResponse;
import com.hglee.account.accounts.dto.SignUpMobileRequest;
import com.hglee.account.accounts.dto.VerifyMobileRequest;
import com.hglee.account.auth.application.IdentityProvider;
import com.hglee.account.auth.application.InteractionProvider;
import com.hglee.account.auth.dto.AuthenticationResponse;
import com.hglee.account.auth.dto.InteractionResponse;
import com.hglee.account.common.dto.ErrorResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;

@Api(tags = "sign-up")
@AllArgsConstructor
@RestController
public class SignUpController {

	private static final String SIGN_UP_PROMPT = "sign-up";
	private final RequestAccountVerificationUseCase requestAccountVerificationUseCase;
	private final VerifyAccountUseCase verifyAccountUseCase;
	private final SignUpWithMobileAndEmailUseCase signUpWithMobileAndEmailUseCase;
	private final IdentityProvider identityProvider;
	private final InteractionProvider interactionProvider;

	@ApiOperation(value = "전화번호 인증코드 요청")
	@ApiResponses({@ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 403, message = "Forbidden", response = ErrorResponse.class),
			@ApiResponse(code = 409, message = "Conflict", response = ErrorResponse.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = ErrorResponse.class)})
	@PostMapping(value = "/sign-up/request-account-verification-mobile", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RequestAccountVerificationMobileResponse> requestAccountVerificationMobile(
			@RequestBody final RequestAccountVerificationMobileRequest request) {
		InteractionResponse interactionResponse = this.interactionProvider.create(SIGN_UP_PROMPT);

		requestAccountVerificationUseCase.execute(new RequestAccountVerificationCommand(request.getMobile()));

		return ResponseEntity.ok(new RequestAccountVerificationMobileResponse(interactionResponse.getInteractionId(),
				request.getMobile()));
	}

	@ApiOperation(value = "전화번호 인증코드 인증 요청")
	@ApiResponses({@ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 403, message = "Forbidden", response = ErrorResponse.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = ErrorResponse.class)})
	@PostMapping(value = "/sign-up/verify-mobile", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> verifyMobile(@RequestBody final VerifyMobileRequest request) {
		this.interactionProvider.verify(request.getInteractionId(), SIGN_UP_PROMPT);

		verifyAccountUseCase.execute(new VerifyAccountCommand(request.getMobile(), request.getCode()));

		return ResponseEntity.ok().build();
	}

	@ApiOperation(value = "전화번호 회원가입 요청")
	@ApiResponses({@ApiResponse(code = 200, message = "Success", response = AuthenticationResponse.class),
			@ApiResponse(code = 403, message = "Forbidden", response = ErrorResponse.class),
			@ApiResponse(code = 409, message = "Conflict", response = ErrorResponse.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = ErrorResponse.class)})
	@PostMapping(value = "/sign-up/mobile", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<AuthenticationResponse> signUpEmailAndMobile(@RequestBody final SignUpMobileRequest request) {
		this.interactionProvider.verify(request.getInteractionId(), SIGN_UP_PROMPT);

		AccountResponseDto signedUpAccount = signUpWithMobileAndEmailUseCase.execute(
				new SignUpWithMobileAndEmailCommand(request.getMobile(), request.getEmail(), request.getPassword(),
						request.getName(), request.getNickName(), request.getCode()));

		AuthenticationResponse authenticationResponse = identityProvider.signInWithMobile(signedUpAccount.getMobile(),
				request.getPassword());

		this.interactionProvider.finish(request.getInteractionId());

		return ResponseEntity.ok(authenticationResponse);
	}

}
