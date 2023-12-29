package com.ourhours.server.domain.meeting.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;

public record MeetingCreateRequest(
	Long organizerId,
	@NotEmpty
	String name,
	@NotEmpty List<@NotEmpty String> candidateDates,

	List<String> candidateTimeZone,
	boolean isCandidateTimeZoneSelected
) {
	public MeetingCreateServiceRequest toServiceRequest() {
		return new MeetingCreateServiceRequest(organizerId, name, candidateDates, candidateTimeZone,
			isCandidateTimeZoneSelected);
	}
}
