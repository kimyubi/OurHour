package com.ourhours.server.domain.meeting.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ourhours.server.domain.meeting.dto.request.MeetingCreateRequest;
import com.ourhours.server.domain.meeting.service.CreateMeetingRoomService;
import com.ourhours.server.global.model.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CreateMeetingApi {

	private final CreateMeetingRoomService createMeetingRoomService;

	@PostMapping("/meeting")
	public ApiResponse<String> createMeeting(@RequestBody @Valid MeetingCreateRequest request) {
		return createMeetingRoomService.createMeeting(request.toServiceRequest());
	}

}
