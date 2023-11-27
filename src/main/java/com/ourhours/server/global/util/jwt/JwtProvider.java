package com.ourhours.server.global.util.jwt;

import static com.ourhours.server.global.model.exception.ExceptionConstant.*;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.ourhours.server.global.model.exception.InvalidUUIDException;
import com.ourhours.server.global.model.exception.JwtException;
import com.ourhours.server.global.model.jwt.dto.response.JwtResponseDto;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.InvalidClaimException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@Component
@ConfigurationProperties("jwt")
public class JwtProvider {

	private static final String USER_ID = JwtConstant.USER_ID.getValue();
	private static final String UUID = JwtConstant.UUID_COOKIE_NAME.getValue();
	private static final String ALG = JwtConstant.ALG.getValue();

	private String secret;
	private Long tokenValidityTime;
	private Key key;

	@PostConstruct
	void init() {
		this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
	}

	public JwtResponseDto generateToken(Long userId, String plainUUID, String encryptedUUID) {
		Map<String, Object> headers = new HashMap<>();
		headers.put(Header.TYPE, Header.JWT_TYPE);
		headers.put(ALG, SignatureAlgorithm.HS256.getValue());

		Claims claims = Jwts.claims();
		claims.put(USER_ID, userId);
		claims.put(UUID, encryptedUUID);

		long now = new Date().getTime();
		Date tokenExpireDate = new Date(now + tokenValidityTime);

		String token = Jwts.builder()
			.setHeader(headers)
			.setClaims(claims)
			.setExpiration(tokenExpireDate)
			.signWith(key, SignatureAlgorithm.HS256)
			.compact();

		return JwtResponseDto.builder().token(token).uuid(plainUUID).tokenExpiredDate(tokenExpireDate).build();
	}

	public Long getUserId(String token, String uuid) throws JwtException, InvalidUUIDException {
		Claims claims = parseClaims(token, uuid);
		return claims.get(USER_ID, Long.class);
	}

	public Claims parseClaims(String token, String uuid) throws JwtException, InvalidUUIDException {
		long clockSkewSeconds = 3 * 60L;

		try {
			return Jwts.parserBuilder()
				.require(UUID, uuid)
				.setAllowedClockSkewSeconds(clockSkewSeconds)
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody();
		} catch (SignatureException e) {
			throw new JwtException(INVALID_SIGNATURE);
		} catch (MalformedJwtException | IllegalArgumentException e) {
			throw new JwtException(INVALID_TOKEN);
		} catch (ExpiredJwtException e) {
			throw new JwtException(EXPIRED_TOKEN);
		} catch (UnsupportedJwtException e) {
			throw new JwtException(UNSUPPORTED_TOKEN);
		} catch (InvalidClaimException e) {
			throw new InvalidUUIDException(INVALID_UUID);
		}
	}
}
