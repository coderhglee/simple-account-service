package com.hglee.account.auth.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.hglee.account.auth.ui.AuthenticationArgumentResolver;

@Configuration
public class AuthenticationConfiguration implements WebMvcConfigurer {
	private final AuthenticationArgumentResolver authenticationArgumentResolver;

	public AuthenticationConfiguration(AuthenticationArgumentResolver authenticationArgumentResolver) {
		this.authenticationArgumentResolver = authenticationArgumentResolver;
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(authenticationArgumentResolver);
	}
}
