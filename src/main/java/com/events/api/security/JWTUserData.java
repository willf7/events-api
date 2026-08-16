package com.events.api.security;

import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record JWTUserData(UUID userId, List<String> roles) {
}
