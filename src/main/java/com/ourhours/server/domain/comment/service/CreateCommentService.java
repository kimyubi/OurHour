package com.ourhours.server.domain.comment.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ourhours.server.domain.comment.dto.request.CreateCommentServiceRequest;
import com.ourhours.server.domain.comment.entity.Comment;
import com.ourhours.server.domain.comment.repository.CommentRepository;
import com.ourhours.server.domain.post.entity.Post;
import com.ourhours.server.domain.post.repository.PostRepository;
import com.ourhours.server.global.exception.ExceptionConstant;
import com.ourhours.server.global.exception.InvalidPostIdException;
import com.ourhours.server.global.model.ApiResponse;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateCommentService {

	private final CommentRepository commentRepository;
	private final PostRepository postRepository;

	public ApiResponse<Long> createComment(CreateCommentServiceRequest request, Long memberId) throws
		InvalidPostIdException {
		validatePost(request.postId());
		Comment comment = request.toEntity(memberId);

		Comment savedComment = commentRepository.save(comment);
		return ApiResponse.ok("새 댓글 작성에 성공했습니다.", savedComment.getId());
	}

	private void validatePost(Long postId) throws
		InvalidPostIdException {
		Optional<Post> optionalPost = postRepository.findById(postId);
		if (optionalPost.isEmpty()) {
			throw new InvalidPostIdException(ExceptionConstant.INVALID_POST_ID);
		}
	}
}
