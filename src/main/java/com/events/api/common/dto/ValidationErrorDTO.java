package com.events.api.common.dto;

public record ValidationErrorDTO(
        String field,
        String message
) {
}
