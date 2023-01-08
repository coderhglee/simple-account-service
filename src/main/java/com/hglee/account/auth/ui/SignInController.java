package com.hglee.account.auth.ui;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hglee.account.auth.application.IdentityProvider;
import com.hglee.account.auth.dto.AuthenticationResponse;
import com.hglee.account.auth.dto.SignInWithEmailRequest;
import com.hglee.account.auth.dto.SignInWithMobileRequest;
import com.hglee.account.common.dto.ErrorResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(tags = "sign-in")
@RestController
public class SignInController {

	private final IdentityProvider identityProvider;

	public SignInController(IdentityProvider identityProvider) {
		this.identityProvider = identityProvider;
	}

	@ApiOperation(value = "전화번호 로그인")
	@ApiResponses({@ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 403, message = "Forbidden", response = ErrorResponse.class),
			@ApiResponse(code = 404, message = "NotFound", response = ErrorResponse.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = ErrorResponse.class)})
	@PostMapping(value = "/sign-in/mobile", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> signInWithMobile(@RequestBody final SignInWithMobileRequest request) {
		AuthenticationResponse authenticationResponse = identityProvider.signInWithMobile(request.getMobile(),
				request.getPassword());

		return ResponseEntity.ok(authenticationResponse);
	}

	@ApiOperation(value = "이메일 로그인")
	@ApiResponses({@ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 403, message = "Forbidden", response = ErrorResponse.class),
			@ApiResponse(code = 404, message = "NotFound", response = ErrorResponse.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = ErrorResponse.class)})
	@PostMapping(value = "/sign-in/email", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> signInWithEmail(@RequestBody final SignInWithEmailRequest request) {
		AuthenticationResponse authenticationResponse = identityProvider.signInWithEmail(request.getEmail(),
				request.getPassword());

		return ResponseEntity.ok(authenticationResponse);
	}
}
