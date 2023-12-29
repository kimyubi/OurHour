package com.ourhours.server.global.model;

import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiResponse<T> {

	// API 상태 코드
	private int code;

	// API 응답 메시지
	private String message;

	// API 응답 데이터
	private T data;

	public static <T> ApiResponse<T> of(int code, String message, T data) {
		return new ApiResponse<>(code, message, data);
	}

	public static <T> ApiResponse<T> ok(String message, T data) {
		return of(HttpStatus.OK.value(), message, data);
	}

	public static <T> ApiResponse<T> okWithMessage(String message) {
		return of(HttpStatus.OK.value(), message, null);
	}

	public static <T> ApiResponse<T> okWithData(T data) {
		return of(HttpStatus.OK.value(), null, data);
	}

}
