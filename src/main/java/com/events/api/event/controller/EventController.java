package com.events.api.event.controller;

import com.events.api.common.dto.PageResponseDTO;
import com.events.api.event.dto.EventDetailsDTO;
import com.events.api.event.dto.EventRequestDTO;
import com.events.api.event.dto.EventResponseDTO;
import com.events.api.event.dto.EventSearchRequestDTO;
import com.events.api.event.dto.EventUpdateRequestDTO;
import com.events.api.security.JWTUserData;
import com.events.api.event.service.EventService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Validated
@RestController
@RequestMapping("/api/event")
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EventResponseDTO> create(@Valid @ModelAttribute EventRequestDTO request) {
        JWTUserData userData = (JWTUserData) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        EventResponseDTO response = eventService.create(request, userData);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping
    public ResponseEntity<PageResponseDTO<EventResponseDTO>> findAll(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(50) int size,
            @ModelAttribute EventSearchRequestDTO request
    ) {
        PageResponseDTO<EventResponseDTO> response = eventService.findAll(page, size, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventDetailsDTO> findById(@PathVariable("eventId") UUID eventId) {
        EventDetailsDTO response = eventService.findById(eventId);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN') or @eventAuth.canModify(#eventId)")
    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, path = "/{eventId}")
    public ResponseEntity<EventResponseDTO> update(@PathVariable("eventId") UUID eventId, @Valid @ModelAttribute EventUpdateRequestDTO request) {
        EventResponseDTO response = eventService.update(eventId, request);

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN') or @eventAuth.canModify(#eventId)")
    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> delete(@PathVariable("eventId") UUID eventId) {
        eventService.delete(eventId);

        return ResponseEntity.noContent().build();
    }
}
