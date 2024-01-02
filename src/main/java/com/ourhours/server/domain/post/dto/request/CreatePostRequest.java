package com.ourhours.server.domain.post.dto.request;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public record CreatePostRequest(
	@Schema(description = "포스트 내용, 필수 값 x")
	String content,

	@Schema(description = "태그 리스트, 필수 값 x")
	List<String> tagList) {
	public CreatePostServiceRequest toServiceRequest() {
		return new CreatePostServiceRequest(content, tagList);
	}
}
