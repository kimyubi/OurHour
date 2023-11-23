package com.ourhours.server.global.model.jwt.dto.response;

import java.util.Date;

import lombok.Builder;

@Builder
public record JwtResponseDto(String token, Date tokenExpiredDate, String uuid) {
}
