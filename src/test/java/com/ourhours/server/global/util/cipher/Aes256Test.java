package com.ourhours.server.global.util.cipher;

import static org.junit.Assert.*;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class Aes256Test {

	@DisplayName("AES-256알고리즘 암호화 테스트")
	@Test
	void aes256EncryptionTest() {
		//given
		String originText = "test";
		String cipherText = "N1Ak85yePvFevd6s7qTgeg==";

		// when
		String aes256EncryptedText = Aes256.encrypt(originText.getBytes(StandardCharsets.UTF_8));

		// then
		assertEquals(aes256EncryptedText, cipherText);
	}

	@Test
	void aes256DecryptionTest() {
		// given
		String originText = "test";
		String cipherText = "N1Ak85yePvFevd6s7qTgeg==";

		// when
		byte[] aes256DecryptedText = Aes256.decrypt(cipherText.getBytes(StandardCharsets.UTF_8));

		// then
		assertArrayEquals(originText.getBytes(StandardCharsets.UTF_8), aes256DecryptedText);
	}

}
