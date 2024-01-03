package com.ourhours.server.domain.comment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record CreateCommentRequest(
	@Schema(description = "댓글 내용")
	@NotBlank(message = "댓글 내용은 빈 문자열, 공백, null일 수 없습니다.")
	String content,

	@Schema(description = "Post 식별자")
	@Positive(message = "Post 식별자는 양수여야 합니다.")
	Long postId

) {
	public CreateCommentServiceRequest toServiceRequest() {
		return new CreateCommentServiceRequest(content(), postId());
	}
}
