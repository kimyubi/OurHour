package com.ourhours.server.global.util.cipher;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Aes256 {
	private static final String TRANSFORMATION = CipherConstants.TRANSFORMATION.getValue();
	private static final String PRIVATE_KEY_AES256 = CipherConstants.PRIVATE_KEY_AES256.getValue();
	private static final String AES = CipherConstants.AES.getValue();
	private static final String CHARSET_NAME = CipherConstants.CHARSET_NAME.getValue();

	public static String encrypt(String text) throws Exception {
		SecretKeySpec secretKey = new SecretKeySpec(PRIVATE_KEY_AES256.getBytes(), AES);
		IvParameterSpec iv = new IvParameterSpec(PRIVATE_KEY_AES256.substring(0, 16).getBytes());

		Cipher cipher = Cipher.getInstance(TRANSFORMATION);
		cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);

		byte[] encrypted = cipher.doFinal(text.getBytes(CHARSET_NAME));
		return Base64.getEncoder().encodeToString(encrypted);
	}

	public static String decrypt(String cipherText) throws Exception {
		SecretKeySpec secretKey = new SecretKeySpec(PRIVATE_KEY_AES256.getBytes(), AES);
		IvParameterSpec iv = new IvParameterSpec(PRIVATE_KEY_AES256.substring(0, 16).getBytes());

		Cipher cipher = Cipher.getInstance(TRANSFORMATION);
		cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);

		byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(cipherText));
		return new String(decrypted, CHARSET_NAME);
	}

}
