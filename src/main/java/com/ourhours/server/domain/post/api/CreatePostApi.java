package com.ourhours.server.domain.post.api;

import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ourhours.server.domain.member.entity.Member;
import com.ourhours.server.domain.member.repository.MemberRepository;
import com.ourhours.server.domain.post.dto.request.CreatePostRequest;
import com.ourhours.server.domain.post.service.CreatePostService;
import com.ourhours.server.global.exception.ExceptionConstant;
import com.ourhours.server.global.model.ApiResponse;
import com.ourhours.server.global.model.security.JwtAuthentication;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "포스트 API")
@RestController
@RequiredArgsConstructor
public class CreatePostApi {

	private final CreatePostService createPostService;
	private final MemberRepository memberRepository;

	@Operation(summary = "새 포스트 작성")
	@PostMapping("/api/post")
	public ApiResponse createPost(@RequestBody CreatePostRequest request) {
		if (!isLoggedIn()) {
			return ApiResponse.withException(ExceptionConstant.INVALID_ACCESS);
		}
		return createPostService.createPost(request.toServiceRequest(), getMemberId());
	}

	private Boolean isLoggedIn() {
		Optional<Member> optionalMember = memberRepository.findById(getMemberId());
		return optionalMember.isPresent();
	}

	private Long getMemberId() {
		JwtAuthentication jwtAuthentication = (JwtAuthentication)SecurityContextHolder.getContext().getAuthentication();
		return jwtAuthentication.getMemberId();
	}
}
