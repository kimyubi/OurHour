package com.ourhours.server.global.model.exception;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {
	private final ExceptionConstant exceptionConstant;

	public BaseException(ExceptionConstant exceptionConstant) {
		super(exceptionConstant.getMessage());
		this.exceptionConstant = exceptionConstant;
	}
}
