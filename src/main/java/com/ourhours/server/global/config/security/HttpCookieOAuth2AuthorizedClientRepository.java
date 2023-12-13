package com.ourhours.server.global.config.security;

import static java.util.Objects.*;

import java.nio.charset.StandardCharsets;
import java.time.Duration;

import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import com.ourhours.server.global.util.cipher.Aes256;
import com.ourhours.server.global.util.jpa.cookie.CookieUtil;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
// TODO : deserialize deprecate (역직렬화 리팩토링)
public class HttpCookieOAuth2AuthorizedClientRepository
	implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

	public static final String OAUTH2_COOKIE_NAME = "OAUTH2_AUTHORIZATION_REQUEST";
	public static final Duration OAUTH_COOKIE_EXPIRY = Duration.ofMinutes(5);

	@Override
	public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
		return getCookie(request);
	}

	@Override
	public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request,
		HttpServletResponse response) {
		if (isNull(authorizationRequest)) {
			removeAuthorizationRequest(request, response);
			return;
		}

		CookieUtil.addCookie(response, OAUTH2_COOKIE_NAME, encrypt(authorizationRequest), OAUTH_COOKIE_EXPIRY);
	}

	@Override
	public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request,
		HttpServletResponse response) {
		OAuth2AuthorizationRequest oAuth2AuthorizationRequest = getCookie(request);
		CookieUtil.removeCookie(request, response, OAUTH2_COOKIE_NAME);
		return oAuth2AuthorizationRequest;
	}

	private OAuth2AuthorizationRequest getCookie(HttpServletRequest request) {
		return CookieUtil.getCookie(request, OAUTH2_COOKIE_NAME).map(this::decrypt).orElse(null);
	}

	private String encrypt(OAuth2AuthorizationRequest authorizationRequest) {
		byte[] bytes = SerializationUtils.serialize(authorizationRequest);
		return Aes256.encrypt(bytes);
	}

	private OAuth2AuthorizationRequest decrypt(Cookie cookie) {
		byte[] bytes = Aes256.decrypt(cookie.getValue().getBytes(StandardCharsets.UTF_8));
		return (OAuth2AuthorizationRequest)SerializationUtils.deserialize(
			bytes);
	}

}



