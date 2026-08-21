package com.events.api.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record LogoutRequestDTO(
        @NotBlank
        String refreshToken
) {}
