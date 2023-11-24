package com.ourhours.server.global.util.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JwtConstant {

	USER_ID("user_id"),
	UUID_COOKIE_NAME("uuid"),
	ALG("alg"),
	JWT_COOKIE_NAME("Authorization");

	private final String value;

}
