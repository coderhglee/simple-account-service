package com.hglee.account.accounts.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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

	@ApiOperation(value = "내정보 조회", authorizations = {@Authorization(value = "jwtToken")})
	@GetMapping(value = "/me")
	@ApiResponses({@ApiResponse(code = 200, message = "Success", response = User.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = ErrorResponse.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = ErrorResponse.class)})
	public ResponseEntity<User> me(@CurrentUser @ApiIgnore() final User user) {
		return ResponseEntity.ok(user);
	}

}
