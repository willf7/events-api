package com.events.api.domain.coupon;

import java.util.UUID;

public record CouponSearchRequestDTO(
        UUID eventId,
        String code,
        Boolean active,
        Boolean valid
) {
}
