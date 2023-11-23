package com.ourhours.server.global.util.jwt;

import static org.junit.Assert.*;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.ourhours.server.IntegrationTestSupporter;
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

	@DisplayName("")
	@Test
	void getUserIdTest() {
		//given
		String plainUUID = UUID.randomUUID().toString();
		String encryptedUUID = Aes256.encrypt(plainUUID);

		Long givenUserId = 1L;
		JwtResponseDto jwtResponseDto = jwtProvider.generateToken(givenUserId, plainUUID,
			encryptedUUID);

		// when
		String token = jwtResponseDto.token();
		Long userIdInToken = jwtProvider.getUserId(token);

		// then
		log.info("Token, userIdInToken : [{}], [{}]", token, userIdInToken);
		assertEquals(givenUserId, userIdInToken);
	}

	@DisplayName("")
	@Test
	void getUUIDTest() {
		//given
		String plainUUID = UUID.randomUUID().toString();
		String encryptedUUID = Aes256.encrypt(plainUUID);

		Long givenUserId = 1L;
		JwtResponseDto jwtResponseDto = jwtProvider.generateToken(givenUserId, plainUUID,
			encryptedUUID);

		// when
		String token = jwtResponseDto.token();
		String UUIDInJwtResponseDto = jwtResponseDto.uuid();
		String UUIDInToken = jwtProvider.getUUID(token);

		// then
		log.info("UUIDInToken, UUIDInJwtResponseDto : [{}], [{}]", UUIDInToken, UUIDInJwtResponseDto);
		assertNotEquals(UUIDInToken, UUIDInJwtResponseDto);
		assertEquals(UUIDInToken, encryptedUUID);
		assertEquals(UUIDInJwtResponseDto, plainUUID);
	}
}
