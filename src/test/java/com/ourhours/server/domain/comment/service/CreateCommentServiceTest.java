package com.ourhours.server.domain.comment.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ourhours.server.IntegrationTestSupporter;
import com.ourhours.server.domain.comment.dto.request.CreateCommentServiceRequest;
import com.ourhours.server.domain.comment.entity.Comment;
import com.ourhours.server.domain.comment.repository.CommentRepository;
import com.ourhours.server.domain.post.entity.Post;
import com.ourhours.server.domain.post.repository.PostRepository;
import com.ourhours.server.global.exception.InvalidPostIdException;
import com.ourhours.server.global.model.ApiResponse;

class CreateCommentServiceTest extends IntegrationTestSupporter {

	@Autowired
	private CreateCommentService createCommentService;

	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private PostRepository postRepository;

	@AfterEach
	void tearDown() {
		postRepository.deleteAllInBatch();
		commentRepository.deleteAllInBatch();
	}

	@DisplayName("요청으로 전달 받은 Post 식별자가 유효하지 않으면 InvalidPostIdException을 던진다.")
	@Test
	void createCommentWithInvalidPostId() {
		//given
		String content = "새 댓글";
		Long memberId = 1L;
		Long postId = 1L;

		CreateCommentServiceRequest request = new CreateCommentServiceRequest(content, postId);

		// when // then
		assertThrows(InvalidPostIdException.class, () -> createCommentService.createComment(request, memberId));
	}

	@DisplayName("요청으로 전달 받은 Post 식별자가 유효하면 새 글 작성에 성공한다.")
	@Test
	void createCommentWithValidPostId() {
		//given
		String content = "새 댓글";
		Long memberId = 1L;
		Long postId = saveNewPost(memberId);

		CreateCommentServiceRequest request = new CreateCommentServiceRequest(content, postId);

		// when
		ApiResponse<Long> response = createCommentService.createComment(request, memberId);
		Long commentId = response.getData();

		Optional<Comment> optionalComment = commentRepository.findById(commentId);

		// then
		assertThat(optionalComment).isNotEmpty();
		Comment comment = optionalComment.get();

		assertThat(comment)
			.extracting("id", "content", "writerId", "postId", "isDeleted")
			.contains(commentId, content, memberId, postId, Boolean.FALSE);
	}

	private Long saveNewPost(Long memberId) {
		Post post = Post
			.builder()
			.content("새 글")
			.memberId(memberId)
			.build();

		Post savedPost = postRepository.save(post);
		return savedPost.getId();
	}

}
