package com.ourhours.server.global.model.exception;

import lombok.Getter;

@Getter
public class InvalidUUIDException extends RuntimeException {
	private final ExceptionConstant exceptionConstant;

	public InvalidUUIDException(ExceptionConstant exceptionConstant) {
		super(exceptionConstant.getMessage());
		this.exceptionConstant = exceptionConstant;
	}
}
