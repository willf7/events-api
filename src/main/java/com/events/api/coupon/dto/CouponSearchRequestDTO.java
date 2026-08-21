package com.events.api.coupon.dto;

import java.util.UUID;

public record CouponSearchRequestDTO(
        UUID eventId,
        String code,
        Boolean active,
        Boolean valid
) {
}
