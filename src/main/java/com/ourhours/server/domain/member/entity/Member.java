package com.ourhours.server.domain.member.entity;

import com.ourhours.server.global.model.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Member extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Long kakaoId;

	@Column(nullable = false)
	private String name;

	// TODO 프로필 이미지

	@Builder
	public Member(String name, Long kakaoId) {
		this.name = name;
		this.kakaoId = kakaoId;
	}

	public void setIdForMockTest(long id) {
		this.id = id;
	}
}
