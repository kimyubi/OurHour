package com.ourhours.server.global.util.jwt;

import java.util.Date;

import lombok.Builder;

@Builder
public record JwtResponseDto(String token, Date tokenExpiredDate, String uuid) {
}
