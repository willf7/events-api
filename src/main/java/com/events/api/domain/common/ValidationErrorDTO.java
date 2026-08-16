package com.events.api.domain.common;

public record ValidationErrorDTO(
        String field,
        String message
) {
}
