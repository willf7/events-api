package com.events.api.domain.coupon;

import com.events.api.domain.event.Event;

public record CouponRequestDTO(Integer discount, String code, long valid) {
}
