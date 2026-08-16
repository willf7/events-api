package com.events.api.controller;

import com.events.api.domain.common.PageResponseDTO;
import com.events.api.domain.event.EventDetailsDTO;
import com.events.api.domain.event.EventRequestDTO;
import com.events.api.domain.event.EventResponseDTO;
import com.events.api.security.JWTUserData;
import com.events.api.service.EventService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/event")
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EventResponseDTO> create(@Valid @ModelAttribute EventRequestDTO eventRequestDTO) {
        JWTUserData userData = (JWTUserData) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        EventResponseDTO newEvent = eventService.createEvent(eventRequestDTO, userData);
        return ResponseEntity.ok(newEvent);
    }

    @GetMapping
    public ResponseEntity<PageResponseDTO<EventResponseDTO>> getEvents(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        PageResponseDTO<EventResponseDTO> events = eventService.getUpcomingEvents(page, size);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/filter")
    public ResponseEntity<PageResponseDTO<EventResponseDTO>> filterEvents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime endDate
    ) {
        title = (title != null) ? title : "";
        city = (city != null) ? city : "";
        state = (state != null) ? state : "";
        startDate = (startDate != null) ? startDate : OffsetDateTime.now();
        endDate = (endDate != null) ? endDate : OffsetDateTime.now().plusYears(100);

        PageResponseDTO<EventResponseDTO> events = eventService.getFilteredEvents(page, size, title, city, state, startDate, endDate);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventDetailsDTO> getEvent(@PathVariable("eventId") UUID eventId) {
        EventDetailsDTO event = eventService.getEventDetails(eventId);
        return ResponseEntity.ok(event);
    }

    @PreAuthorize("hasRole('ADMIN') or @eventAuth.canModify(#eventId)")
    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable("eventId") UUID eventId) {
        eventService.deleteEvent(eventId);

        return ResponseEntity.noContent().build();
    }
}
