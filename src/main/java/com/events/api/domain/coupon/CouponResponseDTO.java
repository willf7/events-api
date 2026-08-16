package com.events.api.domain.coupon;

import java.time.OffsetDateTime;

public record CouponResponseDTO(Integer discount, String code, OffsetDateTime validUntil) {
}
