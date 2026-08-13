package com.events.api.domain.address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AddressRequestDTO(
        @NotBlank(message = "The state can't be empty")
        @Size(min = 2, max = 50, message = "The state must be 2 to 50 characters")
        String state,

        @NotBlank(message = "The city can't be empty")
        @Size(min = 3, max = 50, message = "The city must be 3 to 50 characters")
        String city
) {}
