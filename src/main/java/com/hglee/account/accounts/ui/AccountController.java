package com.hglee.account.accounts.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hglee.account.accounts.application.command.ConfirmPasswordResetCommand;
import com.hglee.account.accounts.application.command.RequestPasswordResetCommand;
import com.hglee.account.accounts.application.command.ResetPasswordCommand;
import com.hglee.account.accounts.application.usecase.ConfirmPasswordResetUseCase;
import com.hglee.account.accounts.application.usecase.RequestPasswordResetUseCase;
import com.hglee.account.accounts.application.usecase.ResetPasswordUseCase;
import com.hglee.account.accounts.dto.PasswordResetRequestDto;
import com.hglee.account.accounts.dto.RequestPasswordResetRequestDto;
import com.hglee.account.accounts.dto.ConfirmPasswordResetRequestDto;
import com.hglee.account.core.CurrentUser;
import com.hglee.account.common.User;
import com.hglee.account.common.dto.ErrorResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = "accounts")
@RestController
public class AccountController {

	private final RequestPasswordResetUseCase requestPasswordResetUseCase;
	private final ConfirmPasswordResetUseCase confirmPasswordResetUseCase;
	private final ResetPasswordUseCase resetPasswordUseCase;

	public AccountController(RequestPasswordResetUseCase requestPasswordResetUseCase,
			ConfirmPasswordResetUseCase confirmPasswordResetUseCase, ResetPasswordUseCase resetPasswordUseCase) {
		this.requestPasswordResetUseCase = requestPasswordResetUseCase;
		this.confirmPasswordResetUseCase = confirmPasswordResetUseCase;
		this.resetPasswordUseCase = resetPasswordUseCase;
	}

	@ApiOperation(value = "내정보 조회", authorizations = {@Authorization(value = "jwtToken")})
	@GetMapping(value = "/me")
	@ApiResponses({@ApiResponse(code = 200, message = "Success", response = User.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = ErrorResponse.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = ErrorResponse.class)})
	public ResponseEntity<User> me(@CurrentUser @ApiIgnore() final User user) {
		return ResponseEntity.ok(user);
	}

	@PostMapping(value = "/request-password-reset")
	@ApiResponses({@ApiResponse(code = 204, message = "Success"),
			@ApiResponse(code = 403, message = "Forbidden", response = ErrorResponse.class),
			@ApiResponse(code = 404, message = "NotFound", response = ErrorResponse.class),
			@ApiResponse(code = 422, message = "Unprocessable Entity", response = ErrorResponse.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = ErrorResponse.class)})
	public ResponseEntity<?> requestPasswordReset(@RequestBody RequestPasswordResetRequestDto dto) {
		requestPasswordResetUseCase.execute(new RequestPasswordResetCommand(dto.getMobile()));

		return ResponseEntity.noContent().build();
	}

	@PostMapping(value = "/confirm-password-reset")
	@ApiResponses({@ApiResponse(code = 204, message = "Success"),
			@ApiResponse(code = 403, message = "Forbidden", response = ErrorResponse.class),
			@ApiResponse(code = 404, message = "NotFound", response = ErrorResponse.class),
			@ApiResponse(code = 422, message = "Unprocessable Entity", response = ErrorResponse.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = ErrorResponse.class)})
	public ResponseEntity<?> confirmPasswordReset(@RequestBody ConfirmPasswordResetRequestDto dto) {
		confirmPasswordResetUseCase.execute(new ConfirmPasswordResetCommand(dto.getMobile(), dto.getCode()));

		return ResponseEntity.noContent().build();
	}

	@PatchMapping(value = "/password")
	@ApiResponses({@ApiResponse(code = 204, message = "Success"),
			@ApiResponse(code = 403, message = "Forbidden", response = ErrorResponse.class),
			@ApiResponse(code = 404, message = "NotFound", response = ErrorResponse.class),
			@ApiResponse(code = 422, message = "Unprocessable Entity", response = ErrorResponse.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = ErrorResponse.class)})
	public ResponseEntity<?> passwordReset(@RequestBody PasswordResetRequestDto dto) {
		resetPasswordUseCase.execute(
				new ResetPasswordCommand(dto.getMobile(), dto.getCode(), dto.getPassword(), dto.getPasswordConfirm()));

		return ResponseEntity.noContent().build();
	}

}
