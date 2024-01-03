package com.ourhours.server.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ourhours.server.global.model.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ApiResponse<String> handleBindException(MethodArgumentNotValidException exception) {
		BindingResult bindingResult = exception.getBindingResult();
		StringBuilder builder = new StringBuilder();

		for (FieldError fieldError : bindingResult.getFieldErrors()) {
			builder.append(" [");
			builder.append(fieldError.getField());
			builder.append("] -> ");
			builder.append(fieldError.getDefaultMessage());
			builder.append(" 입력된 값: [");
			builder.append(fieldError.getRejectedValue());
			builder.append("] ");
		}

		return ApiResponse.withException(ExceptionConstant.INVALID_ARGUMENT,
			builder.toString());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(InvalidPostIdException.class)
	public ApiResponse<String> handleInvalidPostIdException() {
		return ApiResponse.withException(ExceptionConstant.INVALID_POST_ID);
	}
}
