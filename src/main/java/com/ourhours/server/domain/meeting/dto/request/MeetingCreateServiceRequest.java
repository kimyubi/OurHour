package com.ourhours.server.domain.meeting.dto.request;

import java.util.List;

import com.ourhours.server.domain.meeting.entity.Meeting;

public record MeetingCreateServiceRequest(
	Long organizerId,
	String name,
	List<String> candidateDates,
	List<String> candidateTimeZone,
	boolean isCandidateTimeZoneSelected
) {

	public Meeting toEntity(String entryCode) {
		return Meeting
			.builder()
			.organizerId(organizerId)
			.name(name)
			.candidateDates(candidateDates)
			.candidateTimeZone(candidateTimeZone)
			.isCandidateTimeZoneSelected(isCandidateTimeZoneSelected)
			.entryCode(entryCode)
			.build();
	}
}
