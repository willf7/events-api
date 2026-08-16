package com.events.api.config;

import com.events.api.domain.user.Role;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record JWTUserData(UUID userId, List<String> roles) {
}
