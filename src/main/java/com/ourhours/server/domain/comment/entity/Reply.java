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
public class Reply extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, columnDefinition = "text")
	private String content;

	@Column(nullable = false)
	private Long writerId;

	@Column(nullable = false)
	private Long commentId;

	private boolean isDeleted;

	private Reply(String content, Long writerId, Long commentId, boolean isDeleted) {
		this.content = content;
		this.writerId = writerId;
		this.commentId = commentId;
		this.isDeleted = isDeleted;
	}

	public Reply createReply(String content, Long writerId, Long commentId) {
		return new Reply(content, writerId, commentId, Boolean.FALSE);
	}

}