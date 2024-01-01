package com.ourhours.server.global.config.security.filter;

import static com.ourhours.server.global.util.jwt.JwtConstant.*;
import static jakarta.servlet.http.HttpServletResponse.*;
import static org.springframework.http.MediaType.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ourhours.server.global.exception.BaseException;
import com.ourhours.server.global.exception.ExceptionConstant;
import com.ourhours.server.global.exception.ExceptionResponse;
import com.ourhours.server.global.exception.InvalidUUIDException;
import com.ourhours.server.global.exception.JwtException;
import com.ourhours.server.global.util.jpa.cookie.CookieUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtExceptionHandlerFilter extends OncePerRequestFilter {
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		try {
			filterChain.doFilter(request, response);
		} catch (JwtException | InvalidUUIDException exception) {
			setExceptionResponse(response, exception);
			ExceptionConstant exceptionConstant = exception.getExceptionConstant();
			removeCookie(exceptionConstant, request, response);
			log.info("[Exception] Code: [{}], Message : [{}]", exceptionConstant.getCode(),
				exceptionConstant.getMessage());
		}
	}

	private void removeCookie(ExceptionConstant exceptionConstant, HttpServletRequest request,
		HttpServletResponse response) {
		if (exceptionConstant.equals(ExceptionConstant.INVALID_UUID)) {
			log.info("Remove UUID Cookie");
			CookieUtil.removeCookie(request, response, UUID_COOKIE_NAME.getValue());

			log.info("Remove Token Cookie");
			CookieUtil.removeCookie(request, response, JWT_COOKIE_NAME.getValue());
		}
	}

	private void setExceptionResponse(HttpServletResponse response, BaseException exception) throws
		IOException {
		response.setStatus(SC_UNAUTHORIZED);
		response.setContentType(APPLICATION_JSON_VALUE);
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());

		ObjectMapper mapper = new ObjectMapper();
		PrintWriter writer = response.getWriter();
		writer.write(mapper.writeValueAsString(new ExceptionResponse(exception.getExceptionConstant())));
	}

}
