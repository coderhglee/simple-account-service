package com.hglee.account.auth.ui;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hglee.account.auth.application.IdentityProvider;
import com.hglee.account.auth.dto.AuthenticationResponse;
import com.hglee.account.auth.dto.RequestAccountVerificationMobileRequest;
import com.hglee.account.auth.dto.RequestAccountVerificationMobileResponse;
import com.hglee.account.auth.dto.SignUpMobileRequest;
import com.hglee.account.auth.dto.VerifyMobileRequest;
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
	private final IdentityProvider identityProvider;

	@ApiOperation(value = "전화번호 인증코드 요청")
	@ApiResponses({@ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 403, message = "Forbidden", response = ErrorResponse.class),
			@ApiResponse(code = 409, message = "Conflict", response = ErrorResponse.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = ErrorResponse.class)})
	@PostMapping(value = "/sign-up/request-account-verification-mobile", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RequestAccountVerificationMobileResponse> requestAccountVerificationMobile(
			@RequestBody final RequestAccountVerificationMobileRequest request) {

		RequestAccountVerificationMobileResponse requestAccountVerificationMobileResponse = this.identityProvider.requestAccountVerificationMobileForSignUp(
				request.getMobile());

		return ResponseEntity.ok(new RequestAccountVerificationMobileResponse(
				requestAccountVerificationMobileResponse.getInteractionId(),
				requestAccountVerificationMobileResponse.getMobile()));
	}

	@ApiOperation(value = "전화번호 인증코드 인증 요청")
	@ApiResponses({@ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 403, message = "Forbidden", response = ErrorResponse.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = ErrorResponse.class)})
	@PostMapping(value = "/sign-up/verify-mobile", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> verifyMobile(@RequestBody final VerifyMobileRequest request) {
		this.identityProvider.verifyMobileForSignUp(request.getMobile(), request.getCode(), request.getInteractionId());

		return ResponseEntity.ok().build();
	}

	@ApiOperation(value = "전화번호 회원가입 요청")
	@ApiResponses({@ApiResponse(code = 200, message = "Success", response = AuthenticationResponse.class),
			@ApiResponse(code = 403, message = "Forbidden", response = ErrorResponse.class),
			@ApiResponse(code = 409, message = "Conflict", response = ErrorResponse.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = ErrorResponse.class)})
	@PostMapping(value = "/sign-up/mobile", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<AuthenticationResponse> signUpEmailAndMobile(@RequestBody final SignUpMobileRequest request) {
		AuthenticationResponse authenticationResponse = this.identityProvider.signUpWithMobile(request);

		return ResponseEntity.ok(authenticationResponse);
	}

}
