package com.ourhours.server.global.model.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExceptionResponse {

	private String code;
	private String message;

	public ExceptionResponse(ExceptionConstant exceptionConstant) {
		this.code = exceptionConstant.getCode();
		this.message = exceptionConstant.getMessage();
	}
}
