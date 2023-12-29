package com.ourhours.server.domain.meeting.entity;

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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Meeting extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long organizerId;

	@Column(nullable = false, columnDefinition = "text")
	private String name;

	@Type(ListArrayType.class)
	@Column(
		nullable = false,
		name = "candidate_dates",
		columnDefinition = "text[]"
	)
	private List<String> candidateDates;

	@Type(ListArrayType.class)
	@Column(
		name = "candidate_time_zone",
		columnDefinition = "text[]"
	)
	private List<String> candidateTimeZone;

	private boolean isCandidateTimeZoneSelected;

	@Column(
		nullable = false,
		name = "invitation_code",
		columnDefinition = "text"
	)
	private String entryCode;

	@Builder
	public Meeting(Long organizerId, String name, List<String> candidateDates, List<String> candidateTimeZone,
		boolean isCandidateTimeZoneSelected, String entryCode) {
		this.organizerId = organizerId;
		this.name = name;
		this.candidateDates = candidateDates;
		this.candidateTimeZone = candidateTimeZone;
		this.isCandidateTimeZoneSelected = isCandidateTimeZoneSelected;
		this.entryCode = entryCode;
	}
}
