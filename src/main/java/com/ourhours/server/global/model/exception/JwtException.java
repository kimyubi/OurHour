package com.ourhours.server.global.model.exception;

import lombok.Getter;

@Getter
public class JwtException extends RuntimeException {
	private final ExceptionConstant exceptionConstant;

	public JwtException(ExceptionConstant exceptionConstant) {
		super(exceptionConstant.getMessage());
		this.exceptionConstant = exceptionConstant;
	}
}
