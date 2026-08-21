package com.events.api.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record LoginRequestDTO(
        @NotEmpty(message = "E-mail is required")
        @Email
        String email,

        @NotEmpty(message = "Password is required")
        String password
) {
}
