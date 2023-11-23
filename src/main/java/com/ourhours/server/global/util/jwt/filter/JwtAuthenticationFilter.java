package com.ourhours.server.global.util.jwt.filter;

import static com.ourhours.server.global.util.jwt.JwtConstant.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ourhours.server.global.model.exception.JwtException;
import com.ourhours.server.global.model.security.AnonymousAuthentication;
import com.ourhours.server.global.model.security.JwtAuthentication;
import com.ourhours.server.global.model.security.dto.request.JwtAuthenticationRequestDto;
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

	// TODO: UUID 검증하여 유효하지 않은 경우 Exception 던지기
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException, JwtException {

		Cookie[] cookies = request.getCookies();
		if (cookies == null) {
			setAnonymousAuthentication();
			filterChain.doFilter(request, response);
		} else {

			Optional<Cookie> optionalCookie = Arrays.stream(cookies)
				.filter(cookie -> cookie.getName().equals(JWT_COOKIE_NAME.getValue()))
				.findFirst();

			if (optionalCookie.isEmpty()) {
				setAnonymousAuthentication();
				filterChain.doFilter(request, response);
			}

			String token = optionalCookie.get().getValue();

			try {
				Long userId = jwtProvider.getUserId(token);
				setJwtAuthentication(new JwtAuthenticationRequestDto(token, userId));
				filterChain.doFilter(request, response);
			} catch (JwtException jwtException) {
				throw jwtException;
			}
		}
	}

	private void setJwtAuthentication(JwtAuthenticationRequestDto dto) {
		JwtAuthentication jwtAuthentication = new JwtAuthentication(dto);
		SecurityContextHolder.getContext().setAuthentication(jwtAuthentication);
	}

	private void setAnonymousAuthentication() {
		SecurityContextHolder.getContext().setAuthentication(ANONYMOUS_AUTHENTICATION);
	}
}
