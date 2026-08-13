package com.events.api.domain.coupon;

import jakarta.validation.constraints.*;

import java.time.OffsetDateTime;

public record CouponRequestDTO(
        @NotNull(message = "The discount is required")
        @Min(value = 1, message = "The discount must be at least 1%")
        @Max(value = 100, message = "The discount cannot exceed 100%")
        Integer discount,

        @NotBlank(message = "The code can't be empty")
        @Size(min = 3, max = 50, message = "The code must be 3 to 50 characters")
        String code,

        @NotNull(message = "The expiration date is required")
        @Future(message = "The expiration date must be in the future")
        OffsetDateTime validUntil
) {}
