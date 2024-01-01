package com.ourhours.server.global.exception;

import lombok.Getter;

@Getter
public class InvalidUUIDException extends BaseException {
	public InvalidUUIDException(ExceptionConstant exceptionConstant) {
		super(exceptionConstant);
	}
}
