package com.ourhours.server.global.util.jwt;

import static com.ourhours.server.global.model.exception.ExceptionConstant.*;
import static org.junit.Assert.*;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.ourhours.server.IntegrationTestSupporter;
import com.ourhours.server.global.model.exception.InvalidUUIDException;
import com.ourhours.server.global.model.jwt.dto.response.JwtResponseDto;
import com.ourhours.server.global.util.cipher.Aes256;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class JwtProviderTest extends IntegrationTestSupporter {

	@DisplayName("jwt secret 및 tokenValidityTime 주입 테스트")
	@Test
	void jwtPropertyInjectionTest() {
		// Given
		String secret = environment.getProperty("jwt.secret");
		Long tokenValidityTime = Long.parseLong(environment.getProperty("jwt.token-validity-time"));

		assertEquals(jwtProvider.getSecret(), secret);
		assertEquals(jwtProvider.getTokenValidityTime(), tokenValidityTime);
	}

	@DisplayName("토큰과 토큰에 포함된 UUID가 모두 유효하면 토큰을 파싱하여 UserId를 꺼낸다.")
	@Test
	void getUserIdTest() {
		//given
		String plainUUID = UUID.randomUUID().toString();
		String encryptedUUID = Aes256.encrypt(plainUUID.getBytes(StandardCharsets.UTF_8));

		Long givenMemberId = 1L;
		JwtResponseDto jwtResponseDto = jwtProvider.generateToken(givenMemberId, plainUUID,
			encryptedUUID);

		// when
		String token = jwtResponseDto.token();
		Long userIdInToken = jwtProvider.getMemberId(token, encryptedUUID);

		// then
		log.info("Token, userIdInToken : [{}], [{}]", token, userIdInToken);
		assertEquals(givenMemberId, userIdInToken);
	}

	@DisplayName("토큰에 포함된 UUID가 유효하지 않으면, InvalidUUIDException을 던진다.")
	@Test
	void getUserIdTesWithWrongUUID() {
		//given
		String plainUUID = UUID.randomUUID().toString();
		String encryptedUUID = Aes256.encrypt(plainUUID.getBytes(StandardCharsets.UTF_8));
		String wrongUUID = UUID.randomUUID().toString();

		Long givenUserId = 1L;
		JwtResponseDto jwtResponseDto = jwtProvider.generateToken(givenUserId, plainUUID,
			encryptedUUID);

		// when // then
		String token = jwtResponseDto.token();
		assertThrows(INVALID_UUID.getMessage(), InvalidUUIDException.class,
			() -> jwtProvider.getMemberId(token, wrongUUID));

	}

}
