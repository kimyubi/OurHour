package com.ourhours.server.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionConstant {

	// JWT
	INVALID_SIGNATURE(1001, "JWT Signature 검증에 실패하였습니다."),
	EXPIRED_TOKEN(1002, "토큰이 만료되었습니다."),
	INVALID_TOKEN(1003, "올바르게 구성되지 않은 토큰입니다."),
	UNSUPPORTED_TOKEN(1004, "지원하지 않는 형식의 토큰입니다."),
	INVALID_UUID(1005, "자격 증명에 실패하였습니다."),
	FAILED_TO_GET_TOKEN(1006, "쿠키로부터 토큰을 받아오는데 실패하였습니다."),

	// common
	INVALID_ARGUMENT(2001, "요청과 함께 전달된 값이 유효하지 않습니다."),
	INVALID_ACCESS(2002, "잘못된 접근입니다.");

	private final int code;
	private final String message;
}
