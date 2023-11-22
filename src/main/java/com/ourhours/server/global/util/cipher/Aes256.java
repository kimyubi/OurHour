package com.ourhours.server.global.util.cipher;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Aes256 {
	private static final String TRANSFORMATION = CipherConstant.TRANSFORMATION.getValue();
	private static final String PRIVATE_KEY_AES256 = CipherConstant.PRIVATE_KEY_AES256.getValue();
	private static final String AES = CipherConstant.AES.getValue();
	private static final String CHARSET_NAME = CipherConstant.CHARSET_NAME.getValue();

	public static String encrypt(String text) {
		SecretKeySpec secretKey = new SecretKeySpec(PRIVATE_KEY_AES256.getBytes(), AES);
		IvParameterSpec iv = new IvParameterSpec(PRIVATE_KEY_AES256.substring(0, 16).getBytes());
		byte[] encrypted = null;

		try {
			Cipher cipher = Cipher.getInstance(TRANSFORMATION);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
			encrypted = cipher.doFinal(text.getBytes(CHARSET_NAME));

		} catch (NoSuchAlgorithmException
		         | NoSuchPaddingException
		         | InvalidKeyException
		         | InvalidAlgorithmParameterException
		         | IllegalBlockSizeException
		         | UnsupportedEncodingException
		         | BadPaddingException e) {
			log.error("Exception [Message] : {}", e.getMessage());
			log.error("Exception [Location] : {}", e.getStackTrace()[0]);
		}
		return Base64.getEncoder().encodeToString(encrypted);

	}

	public static String decrypt(String cipherText) throws UnsupportedEncodingException {
		SecretKeySpec secretKey = new SecretKeySpec(PRIVATE_KEY_AES256.getBytes(), AES);
		IvParameterSpec iv = new IvParameterSpec(PRIVATE_KEY_AES256.substring(0, 16).getBytes());
		byte[] decrypted = null;

		try {
			Cipher cipher = Cipher.getInstance(TRANSFORMATION);
			cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
			decrypted = cipher.doFinal(Base64.getDecoder().decode(cipherText));

		} catch (NoSuchAlgorithmException
		         | NoSuchPaddingException
		         | InvalidKeyException
		         | InvalidAlgorithmParameterException
		         | IllegalBlockSizeException
		         | BadPaddingException e) {
			log.error("Exception [Message] : {}", e.getMessage());
			log.error("Exception [Location] : {}", e.getStackTrace()[0]);
		}

		return new String(decrypted, CHARSET_NAME);
	}

}
