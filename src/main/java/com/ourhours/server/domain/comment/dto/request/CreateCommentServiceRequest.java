package com.ourhours.server.domain.comment.dto.request;

import com.ourhours.server.domain.comment.entity.Comment;

public record CreateCommentServiceRequest(
	String content,
	Long postId
) {
	public Comment toEntity(Long memberId) {
		return Comment.createComment(content(), memberId, postId());
	}
}
