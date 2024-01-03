package com.ourhours.server.domain.comment.entity;

import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ourhours.server.domain.comment.dto.request.CreateCommentRequest;
import com.ourhours.server.domain.comment.service.CreateCommentService;
import com.ourhours.server.domain.member.entity.Member;
import com.ourhours.server.domain.member.repository.MemberRepository;
import com.ourhours.server.global.exception.ExceptionConstant;
import com.ourhours.server.global.model.ApiResponse;
import com.ourhours.server.global.model.security.JwtAuthentication;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "댓글 API", description = "새 댓글 작성 API")
@RestController
@RequiredArgsConstructor
public class CreateCommentApi {

	private final CreateCommentService createCommentService;
	private final MemberRepository memberRepository;

	@Operation(summary = "새 댓글 작성")
	@PostMapping("/api/comment")
	public ApiResponse<Long> createComment(@RequestBody CreateCommentRequest request) {
		if (Boolean.FALSE.equals(isLoggedIn())) {
			return ApiResponse.withException(ExceptionConstant.INVALID_ACCESS);
		}
		return createCommentService.createComment(request.toServiceRequest(), getMemberId());
	}

	public Boolean isLoggedIn() {
		Optional<Member> optionalMember = memberRepository.findById(getMemberId());
		return optionalMember.isPresent();
	}

	public Long getMemberId() {
		JwtAuthentication jwtAuthentication = (JwtAuthentication)SecurityContextHolder.getContext().getAuthentication();
		return jwtAuthentication.getMemberId();
	}
}
