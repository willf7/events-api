package com.events.api.domain.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record RegisterUserRequestDTO(
        @NotEmpty(message = "Name is required")
        String name,

        @NotEmpty(message = "E-mail is required")
        @Email
        String email,

        @NotEmpty(message = "Password is required")
        @Size(min = 8)
        String password
) {
}
