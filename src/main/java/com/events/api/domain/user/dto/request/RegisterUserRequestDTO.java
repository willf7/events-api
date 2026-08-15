package com.events.api.domain.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record RegisterUserRequestDTO(
        @NotEmpty(message = "Name is required")
        String name,

        @NotEmpty(message = "E-mail is required")
        @Email
        String email,

        @NotEmpty(message = "Password is required")
        String password
) {
}
