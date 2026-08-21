package com.events.api.event.security;

import com.events.api.user.model.Role;
import com.events.api.security.JWTUserData;
import com.events.api.event.repository.EventRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component("eventAuth")
public class EventAuthorization {
    private final EventRepository repository;

    public EventAuthorization(EventRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public boolean canModify(UUID eventId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !(auth.getPrincipal() instanceof JWTUserData user)) {
            return false;
        }

        if (user.roles().contains(Role.ROLE_ADMIN.toString())) {
            return true;
        }

        return repository.findById(eventId)
                .map(event -> event.getOwner() != null && event.getOwner().getId().equals(user.userId()))
                .orElse(false);
    }
}
