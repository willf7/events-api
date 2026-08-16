package com.events.api.domain.event;

import java.time.OffsetDateTime;
import java.util.UUID;

public record EventResponseDTO(
        UUID id,
        String title,
        String description,
        String eventUrl,
        Boolean remote,
        String city,
        String state,
        OffsetDateTime date,
        String imgUrl
) {
}
