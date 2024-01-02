package com.ourhours.server.domain.post.dto.request;

import java.util.List;

import com.ourhours.server.domain.post.entity.Post;

public record CreatePostServiceRequest(String content, List<String> tagList) {

	public Post toPostEntity(Long memberId) {
		return Post
			.builder()
			.memberId(memberId)
			.content(content())
			.build();
	}
}
