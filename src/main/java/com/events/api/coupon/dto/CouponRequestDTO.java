package com.events.api.coupon.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

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
        OffsetDateTime validUntil,

        Boolean active,

        Boolean singleUsePerUser,

        @Min(value = 1, message = "The maximum uses must be at least 1")
        Integer maxUses,

        @NotEmpty(message = "At least one event is required")
        Set<UUID> eventIds
) {}
