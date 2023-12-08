package com.ourhours.server.global.util.cipher;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class Aes256Test {

	@DisplayName("AES-256알고리즘 암호화 테스트")
	@Test
	void aes256EncryptionTest() {
		//given
		byte[] originText = "test".getBytes();
		String cipherText = "N1Ak85yePvFevd6s7qTgeg==";

		// when
		String aes256EncryptedText = Aes256.encrypt(originText);

		// then
		assertEquals(aes256EncryptedText, cipherText);
	}

	@DisplayName("AES-256알고리즘 복호화 테스트")
	@Test
	void aes256DecryptionTest() {
		//given
		byte[] originText = "test".getBytes();
		byte[] cipherText = "N1Ak85yePvFevd6s7qTgeg==".getBytes();

		// when
		byte[] aes256DecryptedText = Aes256.decrypt(cipherText);

		// then
		assertEquals(aes256DecryptedText, originText);
	}
}
