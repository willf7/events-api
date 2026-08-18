package com.events.api.domain.refreshToken;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequestDTO(
        @NotBlank
        String refreshToken
) {}
