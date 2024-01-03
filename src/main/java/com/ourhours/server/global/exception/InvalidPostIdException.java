package com.ourhours.server.global.exception;

import lombok.Getter;

@Getter
public class InvalidPostIdException extends BaseException {
	public InvalidPostIdException(ExceptionConstant exceptionConstant) {
		super(exceptionConstant);
	}
}
