package com.ourhours.server.domain.comment.entity;

import com.ourhours.server.global.model.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, columnDefinition = "text")
	private String content;

	@Column(nullable = false)
	private Long writerId;

	@Column(nullable = false)
	private Long postId;

	private boolean isDeleted;

	private Comment(String content, Long writerId, Long postId, boolean isDeleted) {
		this.content = content;
		this.writerId = writerId;
		this.postId = postId;
		this.isDeleted = isDeleted;
	}

	public static Comment createComment(String content, Long writerId, Long postId) {
		return new Comment(content, writerId, postId, Boolean.FALSE);
	}
}
