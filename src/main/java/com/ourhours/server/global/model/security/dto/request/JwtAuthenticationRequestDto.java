package com.ourhours.server.global.model.security.dto.request;

public record JwtAuthenticationRequestDto(String token, Long userId) {
}
