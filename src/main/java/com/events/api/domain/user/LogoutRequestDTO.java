package com.events.api.domain.user;

import jakarta.validation.constraints.NotBlank;

public record LogoutRequestDTO(
        @NotBlank
        String refreshToken
) {}
