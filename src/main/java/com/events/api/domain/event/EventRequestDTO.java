package com.events.api.domain.event;

import jakarta.validation.constraints.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.OffsetDateTime;

public record EventRequestDTO(
        @NotBlank(message = "The title can't be empty")
        @Size(min = 2, max = 50, message = "The title must be 2 to 50 characters")
        String title,

        @NotBlank(message = "The description can't be empty")
        @Size(max = 50, message = "The description cannot exceed 50 characters")
        String description,

        MultipartFile image,

        @NotBlank(message = "The event url can't be empty")
        @Size(max = 200, message = "The event url cannot exceed 200 characters")
        String eventUrl,

        @NotNull(message = "The remote field is required")
        Boolean remote,

        @Size(min = 3, max = 50, message = "The city must be 3 to 50 characters")
        String city,
        @Size(min = 2, max = 50, message = "The state must be 2 to 50 characters")
        String state,

        @NotNull(message = "The date is required")
        @Future(message = "The date must be in the future")
        OffsetDateTime date
) {
        @AssertTrue(message = "City and state are required when the event is not remote")
        public boolean isValidAddress() {
                if (remote == null) {
                        return true;
                }

                if (!remote) {
                        return city != null && !city.isBlank() && state != null && !state.isBlank();
                }

                return true;
        }
}
