package com.ourhours.server.global.model.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionConstant {

	// JWT
	INVALID_SIGNATURE("0001", "JWT Signature 검증에 실패하였습니다."),
	EXPIRED_TOKEN("0002", "토큰이 만료되었습니다."),
	INVALID_TOKEN("0003", "올바르게 구성되지 않은 토큰입니다."),
	UNSUPPORTED_TOKEN("0004", "지원하지 않는 형식의 토큰입니다."),
	INVALID_UUID("0005", "자격 증명에 실패하였습니다."),
	FAILED_TO_GET_TOKEN("0006", "쿠키로부터 토큰을 받아오는데 실패하였습니다.");

	private final String code;
	private final String message;
}
