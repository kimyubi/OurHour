package com.ourhours.server.global.util.jpa;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.ourhours.server.domain.member.domain.entity.Member;
import com.ourhours.server.domain.member.repository.MemberRepository;
import com.ourhours.server.global.model.security.JwtAuthentication;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomAuditorAware implements AuditorAware<Member> {
	private final MemberRepository memberRepository;

	@Override
	public Optional<Member> getCurrentAuditor() {
		return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
			.filter(Authentication::isAuthenticated)
			.map(authentication -> {
				JwtAuthentication jwtAuthentication = (JwtAuthentication)authentication;
				return memberRepository.findById(jwtAuthentication.getMemberId());
			})
			.orElse(Optional.empty());
	}

}
