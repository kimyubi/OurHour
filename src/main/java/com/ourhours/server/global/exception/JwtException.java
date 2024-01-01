package com.ourhours.server.global.exception;

import lombok.Getter;

@Getter
public class JwtException extends BaseException {
	public JwtException(ExceptionConstant exceptionConstant) {
		super(exceptionConstant);
	}
}
