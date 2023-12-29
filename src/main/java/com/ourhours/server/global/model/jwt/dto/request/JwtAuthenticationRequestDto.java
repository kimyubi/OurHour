package com.ourhours.server.global.model.jwt.dto.request;

public record JwtAuthenticationRequestDto(String token, Long memberId) {
}
