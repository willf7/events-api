package com.events.api.domain.event;

import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.UUID;

public record EventResponseDTO(UUID id, String title, String description, String eventUrl, Boolean remote, String city, String state, Date date, String imgUrl) {
}
