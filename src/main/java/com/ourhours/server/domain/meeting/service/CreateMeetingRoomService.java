package com.ourhours.server.domain.meeting.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ourhours.server.domain.meeting.dto.request.MeetingCreateServiceRequest;
import com.ourhours.server.domain.meeting.entity.Meeting;
import com.ourhours.server.domain.meeting.repository.MeetingRepository;
import com.ourhours.server.global.model.ApiResponse;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CreateMeetingRoomService {

	private final MeetingRepository meetingRepository;

	@Transactional
	public ApiResponse<String> createMeeting(MeetingCreateServiceRequest request) {
		String entryCode = UUID.randomUUID().toString();

		Meeting meeting = request.toEntity(entryCode);
		Meeting savedMeeting = meetingRepository.save(meeting);

		String response = savedMeeting.getEntryCode();
		return ApiResponse.okWithData(response);
	}
}
