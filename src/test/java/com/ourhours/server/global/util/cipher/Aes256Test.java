package com.ourhours.server.global.util.cipher;

import static org.junit.Assert.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ActiveProfiles("cipher")
class Aes256Test {

	@DisplayName("AES-256알고리즘 암호화 테스트")
	@Test
	void aes256EncryptionTest() throws Exception {
		//given
		String originText = "test";
		String cipherText = "N1Ak85yePvFevd6s7qTgeg==";

		// when
		String aes256EncryptedText = Aes256.encrypt(originText);

		// then
		assertEquals(aes256EncryptedText, cipherText);
	}

	@DisplayName("AES-256알고리즘 복호화 테스트")
	@Test
	void aes256DecryptionTest() throws Exception {
		//given
		Aes256 aes256 = new Aes256();
		String originText = "test";
		String cipherText = "N1Ak85yePvFevd6s7qTgeg==";

		// when
		String aes256DecryptedText = Aes256.decrypt(cipherText);

		// then
		assertEquals(aes256DecryptedText, originText);
	}
}
