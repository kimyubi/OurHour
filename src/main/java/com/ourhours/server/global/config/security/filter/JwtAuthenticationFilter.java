package com.ourhours.server.global.config.security.filter;

import static com.ourhours.server.global.model.exception.ExceptionConstant.*;
import static com.ourhours.server.global.util.jwt.JwtConstant.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ourhours.server.global.model.exception.InvalidUUIDException;
import com.ourhours.server.global.model.exception.JwtException;
import com.ourhours.server.global.model.jwt.dto.request.JwtAuthenticationRequestDto;
import com.ourhours.server.global.model.security.AnonymousAuthentication;
import com.ourhours.server.global.model.security.JwtAuthentication;
import com.ourhours.server.global.util.cipher.Aes256;
import com.ourhours.server.global.util.jwt.JwtProvider;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private static final AnonymousAuthentication ANONYMOUS_AUTHENTICATION = new AnonymousAuthentication();
	private final JwtProvider jwtProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException, JwtException, InvalidUUIDException {
		Optional<Cookie[]> optionalCookies = Optional.ofNullable(request.getCookies());

		if (optionalCookies.isEmpty() || isJwtCookieMissing(optionalCookies.get())) {
			setAnonymousAuthentication();
			filterChain.doFilter(request, response);
			return;
		}

		Cookie[] cookies = optionalCookies.get();
		String token = getToken(cookies);
		String uuid = getUuid(cookies);

		Long userId = jwtProvider.getMemberId(token, Aes256.encrypt(uuid.getBytes(StandardCharsets.UTF_8)));
		setJwtAuthentication(new JwtAuthenticationRequestDto(token, userId));
		filterChain.doFilter(request, response);
	}

	private boolean isJwtCookieMissing(Cookie[] cookies) {
		return Arrays.stream(cookies)
			.noneMatch(cookie -> cookie.getName().equals(JWT_COOKIE_NAME.getValue()));
	}

	private String getToken(Cookie[] cookies) {
		return Arrays.stream(cookies)
			.filter(cookie -> cookie.getName().equals(JWT_COOKIE_NAME.getValue()))
			.findFirst()
			.map(Cookie::getValue)
			.orElseThrow(() -> new JwtException(FAILED_TO_GET_TOKEN));
	}

	private String getUuid(Cookie[] cookies) {
		return Arrays.stream(cookies)
			.filter(cookie -> cookie.getName().equals(UUID_COOKIE_NAME.getValue()))
			.findFirst()
			.map(Cookie::getValue)
			.orElseThrow(() -> new InvalidUUIDException(INVALID_UUID));
	}

	private void setJwtAuthentication(JwtAuthenticationRequestDto dto) {
		JwtAuthentication jwtAuthentication = new JwtAuthentication(dto);
		SecurityContextHolder.getContext().setAuthentication(jwtAuthentication);
	}

	private void setAnonymousAuthentication() {
		SecurityContextHolder.getContext().setAuthentication(ANONYMOUS_AUTHENTICATION);
	}
}
