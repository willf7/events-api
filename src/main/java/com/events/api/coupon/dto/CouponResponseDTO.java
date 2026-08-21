package com.events.api.coupon.dto;

import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

public record CouponResponseDTO(
        UUID id,
        Integer discount,
        String code,
        OffsetDateTime validUntil,
        Boolean active,
        Boolean singleUsePerUser,
        Integer maxUses,
        Integer usesCount,
        Set<UUID> eventIds,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}
