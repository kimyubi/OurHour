package com.ourhours.server.global.config.security;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.ourhours.server.domain.member.entity.Member;
import com.ourhours.server.domain.member.repository.MemberRepository;
import com.ourhours.server.global.model.security.Role;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

	private final MemberRepository memberRepository;
	public static final String ATTRIBUTE_KEY = "memberId";

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();
		OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

		Map<String, Object> kakaoAccount = (Map<String, Object>)oAuth2User.getAttributes().get("kakao_account");
		Map<String, Object> kakaoProfile = (Map<String, Object>)kakaoAccount.get("profile");
		String name = (String)kakaoProfile.get("nickname");
		Long kakaoId = (Long)oAuth2User.getAttributes().get("id");

		Map<String, Object> attributes = new HashMap<>();
		attributes.put(ATTRIBUTE_KEY, getOrSaveMember(kakaoId, name).getId());

		return new DefaultOAuth2User(
			Collections.singleton(new SimpleGrantedAuthority(Role.USER.name())),
			attributes, ATTRIBUTE_KEY);
	}

	private Member getOrSaveMember(Long kakaoId, String name) {
		Optional<Member> optionalMember = memberRepository.findByKakaoId(kakaoId);
		if (optionalMember.isEmpty())
			return saveMember(kakaoId, name);

		return optionalMember.get();
	}

	private Member saveMember(Long kakaoId, String name) {
		Member member = Member.builder()
			.name(name)
			.kakaoId(kakaoId)
			.build();
		return memberRepository.save(member);
	}
}

