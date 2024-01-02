package com.ourhours.server.domain.post.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ourhours.server.IntegrationTestSupporter;
import com.ourhours.server.domain.post.dto.request.CreatePostServiceRequest;
import com.ourhours.server.domain.post.entity.Post;
import com.ourhours.server.domain.post.entity.Tag;
import com.ourhours.server.domain.post.repository.PostRepository;
import com.ourhours.server.domain.post.repository.TagRepository;
import com.ourhours.server.global.model.ApiResponse;

class CreatePostServiceTest extends IntegrationTestSupporter {

	@Autowired
	private CreatePostService createPostService;

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private TagRepository tagRepository;

	@AfterEach
	void tearDown() {
		postRepository.deleteAllInBatch();
		tagRepository.deleteAllInBatch();
	}

	@DisplayName("새 포스트 작성 성공 테스트 - 태그가 없는 케이스")
	@Test
	void createPostWithoutTagList() {
		//given
		String content = "새 포스팅";
		List<String> tagNameList = List.of();
		Long memberId = 1L;

		CreatePostServiceRequest request = new CreatePostServiceRequest(content, tagNameList);

		// when
		ApiResponse response = createPostService.createPost(request, memberId);
		Long postId = (Long)response.getData();

		Optional<Post> optionalPost = postRepository.findById(postId);

		// then
		assertThat(optionalPost).isNotEmpty();
		Post post = optionalPost.get();
		assertThat(post)
			.extracting("id", "content", "memberId")
			.contains(post.getId(), content, memberId);

	}

	@DisplayName("새 포스트 작성 성공 테스트 - 태그가 있는 케이스")
	@Test
	void createPostWithNewTagList() {
		//given
		String content = "새 포스팅";
		List<String> tagNameList = List.of("추리소설", "히가시노 게이고");
		Long memberId = 1L;

		CreatePostServiceRequest request = new CreatePostServiceRequest(content, tagNameList);

		// when
		ApiResponse response = createPostService.createPost(request, memberId);
		Long postId = (Long)response.getData();

		Optional<Post> optionalPost = postRepository.findById(postId);
		List<Tag> tagList = tagRepository.findByNameIn(tagNameList);

		// then
		assertThat(optionalPost).isNotEmpty();
		Post post = optionalPost.get();
		assertThat(post)
			.extracting("id", "content", "memberId")
			.contains(post.getId(), content, memberId);

		assertThat(tagList).isNotEmpty();
		for (Tag tag : tagList) {
			assertTrue(tag.getPostIdList().contains(postId));
		}
	}
}
