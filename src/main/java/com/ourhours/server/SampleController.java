package com.ourhours.server;

import static com.ourhours.server.global.util.jwt.JwtConstant.*;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ourhours.server.global.model.jwt.dto.response.JwtResponseDto;
import com.ourhours.server.global.model.security.JwtAuthentication;
import com.ourhours.server.global.util.cipher.Aes256;
import com.ourhours.server.global.util.jwt.JwtProvider;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SampleController {
	private final JwtProvider jwtProvider;

	@GetMapping("/api/token")
	public JwtResponseDto generateToken(HttpServletResponse response) {
		String uuid = java.util.UUID.randomUUID().toString();
		String encryptedUuid = Aes256.encrypt(uuid);
		JwtResponseDto jwtResponseDto = jwtProvider.generateToken(1L, uuid, encryptedUuid);
		addCookie(response, jwtResponseDto);
		log.info("generate Token: Token [{}], UUID [{}], UUID IN TOKEN [{}]", jwtResponseDto.token(),
			jwtResponseDto.uuid(), encryptedUuid);
		return jwtResponseDto;
	}

	private void addCookie(HttpServletResponse response, JwtResponseDto jwtResponseDto) {
		Cookie jwtCookie = new Cookie(JWT_COOKIE_NAME.getValue(), jwtResponseDto.token());
		jwtCookie.setPath("/");
		jwtCookie.setMaxAge(14 * 24 * 60 * 60);

		Cookie uuidCookie = new Cookie(UUID_COOKIE_NAME.getValue(), jwtResponseDto.uuid());
		uuidCookie.setPath("/");
		uuidCookie.setMaxAge(14 * 24 * 60 * 60);

		response.addCookie(jwtCookie);
		response.addCookie(uuidCookie);

		log.info("add Cookie: Cookie Name [{}], Cookie Value [{}], ", JWT_COOKIE_NAME.getValue(),
			jwtResponseDto.token());
		log.info("add Cookie: Cookie Name [{}], Cookie Value [{}], ", UUID_COOKIE_NAME.getValue(),
			jwtResponseDto.uuid());
	}

	@GetMapping("/api/user")
	public Long getUserId() {
		JwtAuthentication authentication = (JwtAuthentication)SecurityContextHolder.getContext().getAuthentication();
		return authentication.getUserId();
	}
}
