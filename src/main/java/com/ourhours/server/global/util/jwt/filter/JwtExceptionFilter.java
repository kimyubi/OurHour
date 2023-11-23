package com.ourhours.server.global.util.jwt.filter;

import static jakarta.servlet.http.HttpServletResponse.*;
import static org.springframework.http.MediaType.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ourhours.server.global.model.exception.ExceptionResponse;
import com.ourhours.server.global.model.exception.JwtException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtExceptionFilter extends OncePerRequestFilter {
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		try {
			filterChain.doFilter(request, response);
		} catch (JwtException exception) {
			setExceptionResponse(response, exception);
			log.info("JwtException [message] : [{}]", exception.getMessage());
		}
		// TODO InvalidUUIDException Catch 로직 작성
	}

	private void setExceptionResponse(HttpServletResponse response, JwtException exception) throws IOException {
		response.setStatus(SC_UNAUTHORIZED);
		response.setContentType(APPLICATION_JSON_VALUE);
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());

		ObjectMapper mapper = new ObjectMapper();
		PrintWriter writer = response.getWriter();
		writer.write(mapper.writeValueAsString(new ExceptionResponse(exception.getExceptionConstant())));
	}
}
