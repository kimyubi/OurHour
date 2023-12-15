package com.ourhours.server.global.config.security;

import static com.ourhours.server.global.config.security.CustomOAuth2UserService.*;
import static com.ourhours.server.global.util.jwt.JwtConstant.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.ourhours.server.global.model.jwt.dto.response.JwtResponseDto;
import com.ourhours.server.global.util.cipher.Aes256;
import com.ourhours.server.global.util.jpa.cookie.CookieUtil;
import com.ourhours.server.global.util.jwt.JwtProvider;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class OauthAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private final JwtProvider jwtProvider;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException {
		OAuth2User oAuth2User = (OAuth2User)authentication.getPrincipal();

		Long memberId = oAuth2User.getAttribute(ATTRIBUTE_KEY);
		generateToken(response, memberId);

		getRedirectStrategy().sendRedirect(request, response, "/");
	}

	public JwtResponseDto generateToken(HttpServletResponse response, Long memberId) {
		String uuid = java.util.UUID.randomUUID().toString();
		String encryptedUuid = Aes256.encrypt(uuid.getBytes(StandardCharsets.UTF_8));

		JwtResponseDto jwtResponseDto = jwtProvider.generateToken(memberId, uuid, encryptedUuid);
		log.info("generate Token: Token [{}], UUID [{}], UUID IN TOKEN [{}]", jwtResponseDto.token(),
			jwtResponseDto.uuid(), encryptedUuid);

		addCookie(response, jwtResponseDto);
		return jwtResponseDto;
	}

	private void addCookie(HttpServletResponse response, JwtResponseDto jwtResponseDto) {
		CookieUtil.addCookie(response, JWT_COOKIE_NAME.getValue(), jwtResponseDto.token(), Duration.ofDays(14));
		CookieUtil.addCookie(response, UUID_COOKIE_NAME.getValue(), jwtResponseDto.uuid(), Duration.ofDays(14));
		log.info("add Cookie: Cookie Name [{}], Cookie Value [{}], ", JWT_COOKIE_NAME.getValue(),
			jwtResponseDto.token());
		log.info("add Cookie: Cookie Name [{}], Cookie Value [{}], ", UUID_COOKIE_NAME.getValue(),
			jwtResponseDto.uuid());
	}

}
