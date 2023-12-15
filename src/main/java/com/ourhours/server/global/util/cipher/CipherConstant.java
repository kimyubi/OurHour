package com.ourhours.server.global.util.cipher;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CipherConstant {

	TRANSFORMATION("AES/CBC/PKCS5Padding"),
	PRIVATE_KEY_AES256("2023_KIMYUBI_AES_256_PRIVATE_KEY"),
	AES("AES");

	private final String value;
}
