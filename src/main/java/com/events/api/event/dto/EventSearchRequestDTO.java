package com.events.api.event.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.OffsetDateTime;

public record EventSearchRequestDTO(
        String title,
        String city,
        String state,

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        OffsetDateTime startDate,

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        OffsetDateTime endDate
) {}
