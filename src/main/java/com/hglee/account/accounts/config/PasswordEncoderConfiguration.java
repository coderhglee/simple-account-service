package com.hglee.account.accounts.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

@Configuration
public class PasswordEncoderConfiguration {
	@Bean
	public PasswordEncoder passwordEncoder(@Value("${password.secret}") String secret) {
		return new Pbkdf2PasswordEncoder(secret, 16);
	}

}
