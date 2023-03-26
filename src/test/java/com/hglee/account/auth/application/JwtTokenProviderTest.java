package com.hglee.account.auth.application;

import static org.assertj.core.api.BDDAssertions.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;

import com.hglee.account.auth.dto.AccessTokenDecodedDto;
import com.hglee.account.auth.dto.AccessTokenEncodedDto;
import com.hglee.account.auth.dto.CreateOauth2TokenDto;
import com.hglee.account.auth.infrastructure.security.JwtTokenProvider;
import com.hglee.account.auth.infrastructure.security.TokenProvider;

@SpringBootTest
class JwtTokenProviderTest {

	@Autowired
	JwtEncoder encoder;

	@Autowired
	JwtDecoder decoder;

	TokenProvider tokenProvider;

	@BeforeEach
	void before() {
		tokenProvider = new JwtTokenProvider(encoder, decoder);
	}

	@Nested
	@DisplayName("Context: 토큰 발급을 요청하면")
	class issued_access_token {
		@DisplayName("It: 유효한 token이 발급된다.")
		@Test
		void issued_valid_jwt_token() {
			AccessTokenEncodedDto oAuth2Token = tokenProvider.encode(
					new CreateOauth2TokenDto(LocalDateTime.now().plusMinutes(1L), LocalDateTime.now(), "some"));

			then(oAuth2Token.getAccessToken()).isExactlyInstanceOf(String.class);
			then(tokenProvider.decode(oAuth2Token.getAccessToken()).get().getSub()).isEqualTo("some");
		}
	}

	@Nested
	@DisplayName("Context: 만료된 토큰을 decode 하면")
	class expires_token_decode {
		@DisplayName("It: null을 반환한다.")
		@Test
		void it_throw_error() {
			LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
			AccessTokenEncodedDto accessTokenEncodedDto = tokenProvider.encode(
					new CreateOauth2TokenDto(now.plusNanos(10),
							now, "some"));

			Optional<AccessTokenDecodedDto> decode = tokenProvider.decode(accessTokenEncodedDto.getAccessToken());

			then(decode.isEmpty()).isTrue();
		}

	}

}