package com.ourhours.server.global.model.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.ourhours.server.global.model.security.dto.request.JwtAuthenticationRequestDto;

import lombok.Getter;

@Getter
public class JwtAuthentication implements Authentication {

	private final String token;
	private final Long userId;
	private boolean isAuthenticated;

	public JwtAuthentication(JwtAuthenticationRequestDto dto) {
		this.token = dto.token();
		this.userId = dto.userId();
		this.isAuthenticated = true;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return isAuthenticated ? Collections.singletonList(new SimpleGrantedAuthority(Role.USER.name())) :
			Collections.singletonList(new SimpleGrantedAuthority(Role.ANONYMOUS.name()));
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
		return this.isAuthenticated;
	}

	@Override
	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
		this.isAuthenticated = isAuthenticated;

	}

	@Override
	public String getName() {
		return null;
	}
}
