package com.ourhours.server.global.util.cipher;

import java.nio.charset.StandardCharsets;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CipherConstant {
	// TODO: GitHub Secret 에서 관리할 파일

	TRANSFORMATION("AES/CBC/PKCS5Padding"),
	PRIVATE_KEY_AES256("2023_KIMYUBI_AES_256_PRIVATE_KEY"),
	AES("AES"),
	CHARSET_NAME(StandardCharsets.UTF_8.name());

	private final String value;
}
