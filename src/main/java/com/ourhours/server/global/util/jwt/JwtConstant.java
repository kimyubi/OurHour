package com.ourhours.server.global.util.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JwtConstant {

	USER_ID("user_id"),
	UUID("uuid"),
	ALG("alg");

	private final String value;

}
