package com.ourhours.server.global.config.security.filter;

import static com.ourhours.server.global.exception.ExceptionConstant.*;
import static com.ourhours.server.global.util.jwt.JwtConstant.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ourhours.server.global.exception.InvalidUUIDException;
import com.ourhours.server.global.exception.JwtException;
import com.ourhours.server.global.model.jwt.dto.request.JwtAuthenticationRequestDto;
import com.ourhours.server.global.model.security.AnonymousAuthentication;
import com.ourhours.server.global.model.security.JwtAuthentication;
import com.ourhours.server.global.util.cipher.Aes256;
import com.ourhours.server.global.util.jpa.cookie.CookieUtil;
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
		Optional<Cookie> optionalJwtCookie = CookieUtil.getCookie(request, JWT_COOKIE_NAME.getValue());

		if (optionalJwtCookie.isEmpty()) {
			setAnonymousAuthentication();
			filterChain.doFilter(request, response);
			return;
		}

		String token = optionalJwtCookie.get().getValue();
		String uuid = getUuidFromCookie(request);

		Long memberId = jwtProvider.getMemberId(token, Aes256.encrypt(uuid.getBytes(StandardCharsets.UTF_8)));
		setJwtAuthentication(new JwtAuthenticationRequestDto(token, memberId));
		filterChain.doFilter(request, response);
	}

	private String getUuidFromCookie(HttpServletRequest request) {
		return CookieUtil.getCookie(request, UUID_COOKIE_NAME.getValue())
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
