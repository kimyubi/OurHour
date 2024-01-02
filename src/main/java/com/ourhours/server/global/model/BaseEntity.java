package com.ourhours.server.global.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.ourhours.server.domain.member.entity.Member;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

	@CreatedDate
	private LocalDateTime createdAt;

	@CreatedBy
	@ManyToOne
	private Member createdBy;

	@LastModifiedDate
	private LocalDateTime modifiedAt;

	@LastModifiedBy
	@ManyToOne
	private Member modifiedBy;

}
