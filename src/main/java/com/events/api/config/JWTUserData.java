package com.events.api.config;

import lombok.Builder;

import java.util.UUID;

@Builder
public record JWTUserData(UUID userId, String role) {
}
