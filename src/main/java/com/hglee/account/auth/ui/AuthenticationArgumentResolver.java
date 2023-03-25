package com.hglee.account.auth.ui;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.hglee.account.auth.dto.AccountResponseDto;
import com.hglee.account.auth.exception.AuthenticationException;
import com.hglee.account.auth.infrastructure.client.IAccountManagementClient;
import com.hglee.account.core.CurrentUser;
import com.hglee.account.auth.infrastructure.security.TokenProvider;
import com.hglee.account.common.User;
import com.hglee.account.auth.dto.AccessTokenDecodedDto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class AuthenticationArgumentResolver implements HandlerMethodArgumentResolver {

	private final TokenProvider tokenProvider;
	private final IAccountManagementClient accountManagementClient;

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(CurrentUser.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
		HttpServletRequest httpServletRequest = Objects.requireNonNull(
				webRequest.getNativeRequest(HttpServletRequest.class));

		String authorizationHeader = httpServletRequest.getHeader("Authorization");

		if (authorizationHeaderIsInvalid(authorizationHeader)) {
			throw new IllegalArgumentException();
		}

		String token = authorizationHeader.replace("Bearer ", "");

		AccessTokenDecodedDto accessTokenDecodedDto = tokenProvider.decode(token)
				.orElseThrow(AuthenticationException::new);

		String sub = accessTokenDecodedDto.getSub();

		AccountResponseDto accountResponseDto = accountManagementClient.findById(sub);

		return new User(accountResponseDto.getId(), accountResponseDto.getStatus(), accountResponseDto.getEmail(),
				accountResponseDto.getMobile(), accountResponseDto.getName(), accountResponseDto.getNickName());
	}

	private boolean authorizationHeaderIsInvalid(String authorizationHeader) {
		return authorizationHeader == null || !authorizationHeader.startsWith("Bearer ");
	}
}
