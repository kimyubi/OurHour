package com.ourhours.server.global.model.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.ourhours.server.global.model.jwt.dto.request.JwtAuthenticationRequestDto;

import lombok.Getter;

@Getter
public class JwtAuthentication implements Authentication {

	private final String token;
	private final Long memberId;

	public JwtAuthentication(JwtAuthenticationRequestDto dto) {
		this.token = dto.token();
		this.memberId = dto.memberId();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singletonList(new SimpleGrantedAuthority(Role.USER.name()));
	}

	@Override
	public Object getCredentials() {
		return this.token;
	}

	@Override
	public Object getDetails() {
		return null;
	}

	@Override
	public Object getPrincipal() {
		return null;
	}

	@Override
	public boolean isAuthenticated() {
		return true;
	}

	@Override
	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getName() {
		return null;
	}
}
