package com.ourhours.server.domain.post.entity;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.Type;

import com.ourhours.server.global.model.BaseEntity;
import com.vladmihalcea.hibernate.type.array.ListArrayType;

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
public class Tag extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Type(ListArrayType.class)
	@Column(columnDefinition = "bigint[]")
	private List<Long> postIdList = new ArrayList<>();

	@Column(nullable = false,
		columnDefinition = "text"
	)
	private String name;

	private Tag(String name) {
		this.name = name;
	}

	public static Tag createTag(String tagName) {
		return new Tag(tagName);
	}

	public void addPostId(Long postId) {
		postIdList.add(postId);
	}
}
