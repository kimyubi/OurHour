package com.ourhours.server.domain.meeting.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ourhours.server.domain.meeting.entity.Meeting;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {
}
