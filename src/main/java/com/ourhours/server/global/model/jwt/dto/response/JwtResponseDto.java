package com.ourhours.server.global.model.jwt.dto.response;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Builder;

@Builder
public record JwtResponseDto(String token, Date tokenExpiredDate, @JsonIgnore String uuid) {
}
