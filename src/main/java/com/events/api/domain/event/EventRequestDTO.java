package com.events.api.domain.event;

import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

public record EventRequestDTO(String title, String description, MultipartFile image, String eventUrl, Boolean remote, String city, String state, Long date) {
}
