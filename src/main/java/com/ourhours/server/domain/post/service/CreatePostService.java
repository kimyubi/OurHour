package com.ourhours.server.domain.post.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ourhours.server.domain.post.dto.request.CreatePostServiceRequest;
import com.ourhours.server.domain.post.entity.Post;
import com.ourhours.server.domain.post.entity.Tag;
import com.ourhours.server.domain.post.repository.PostRepository;
import com.ourhours.server.domain.post.repository.TagRepository;
import com.ourhours.server.global.model.ApiResponse;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CreatePostService {

	private final PostRepository postRepository;

	private final TagRepository tagRepository;

	public ApiResponse createPost(CreatePostServiceRequest request, Long memberId) {
		Post post = request.toPostEntity(memberId);
		Post savedPost = postRepository.save(post);
		Long postId = savedPost.getId();

		if (hasTags(request)) {
			request.tagList().forEach(tagName -> createOrUpdateTag(postId, tagName));
		}
		return ApiResponse.ok("새 포스트 작성에 성공했습니다.", postId);
	}

	private void createOrUpdateTag(Long postId, String tagName) {
		Optional<Tag> optionalTag = tagRepository.findByName(tagName);
		if (optionalTag.isEmpty()) {
			tagRepository.save(createTag(postId, tagName));
			return;
		}
		Tag tag = optionalTag.get();
		tag.addPostId(postId);
	}

	private boolean hasTags(CreatePostServiceRequest request) {
		return request.tagList() != null && !request.tagList().isEmpty();
	}

	private Tag createTag(Long postId, String tagName) {
		Tag tag = Tag.createTag(tagName);
		tag.addPostId(postId);
		return tag;
	}
}
