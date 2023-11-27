package com.ourhours.server.global.model.exception;

import lombok.Getter;

@Getter
public class JwtException extends BaseException {
	public JwtException(ExceptionConstant exceptionConstant) {
		super(exceptionConstant);
	}
}
