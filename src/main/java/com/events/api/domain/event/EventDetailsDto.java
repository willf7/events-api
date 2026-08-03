package com.events.api.domain.event;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public record EventDetailsDto(
        UUID id,
        String title,
        String description,
        String eventUrl,
        Boolean remote,
        String city,
        String state,
        Date date,
        String imgUrl,
        List<CouponDTO> coupons
) {
    public record CouponDTO(
            String code,
            Integer discount,
            Date validUntil
    ) {}
}
