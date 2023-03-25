package com.hglee.account.auth.infrastructure.security;

import java.time.ZoneOffset;
import java.util.Optional;

import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import com.hglee.account.auth.dto.CreateOauth2TokenDto;
import com.hglee.account.auth.dto.AccessTokenDecodedDto;

@Service
public class JwtTokenProvider implements TokenProvider {
	private final JwtEncoder jwtEncoder;
	private final JwtDecoder jwtDecoder;

	public JwtTokenProvider(JwtEncoder jwtEncoder, JwtDecoder jwtDecoder) {
		this.jwtEncoder = jwtEncoder;
		this.jwtDecoder = jwtDecoder;
	}

	@Override
	public Optional<AccessTokenDecodedDto> decode(String token) {
		AccessTokenDecodedDto accessTokenDecodedDto = getAccessTokenDecodedDto(token);
		return Optional.ofNullable(accessTokenDecodedDto);
	}

	private AccessTokenDecodedDto getAccessTokenDecodedDto(String token) {
		try {
			Jwt decode = jwtDecoder.decode(token);
			return new AccessTokenDecodedDto(decode.getIssuedAt(), decode.getExpiresAt(), decode.getSubject());
		} catch (BadJwtException badJwtException) {
			return null;
		}
	}

	@Override
	public OAuth2Token encode(CreateOauth2TokenDto dto) {
		JwtClaimsSet claims = JwtClaimsSet.builder()
				.issuer("https://simple-account-service")
				.issuedAt(dto.getIssuedAt().toInstant(ZoneOffset.UTC))
				.expiresAt(dto.getExpiresAt().toInstant(ZoneOffset.UTC))
				.subject(dto.getSubject())
				.build();

		return this.jwtEncoder.encode(JwtEncoderParameters.from(claims));
	}
}
