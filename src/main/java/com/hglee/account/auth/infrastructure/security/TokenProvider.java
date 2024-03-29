package com.hglee.account.auth.infrastructure.security;

import java.util.Optional;

import org.springframework.security.oauth2.core.OAuth2Token;

import com.hglee.account.auth.dto.AccessTokenDecodedDto;
import com.hglee.account.auth.dto.AccessTokenEncodedDto;
import com.hglee.account.auth.dto.CreateOauth2TokenDto;

public interface TokenProvider {
	Optional<AccessTokenDecodedDto> decode(String token);

	AccessTokenEncodedDto encode(CreateOauth2TokenDto dto);
}
