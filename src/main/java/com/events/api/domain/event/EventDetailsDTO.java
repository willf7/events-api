package com.events.api.domain.event;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record EventDetailsDTO(
        UUID id,
        String title,
        String description,
        String eventUrl,
        Boolean remote,
        String city,
        String state,
        OffsetDateTime date,
        String imgUrl,
        List<CouponDTO> coupons
) {
    public record CouponDTO(
            UUID id,
            String code,
            Integer discount,
            OffsetDateTime validUntil,
            Boolean active,
            Boolean singleUsePerUser,
            Integer maxUses,
            Integer usesCount
    ) {}
}
