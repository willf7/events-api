package com.events.api.service;

import com.events.api.domain.coupon.Coupon;
import com.events.api.domain.coupon.CouponRequestDTO;
import com.events.api.domain.event.Event;
import com.events.api.exceptions.EntityNotFoundException;
import com.events.api.repositories.CouponRepository;
import com.events.api.repositories.EventRepository;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class CouponService {
    private final CouponRepository repository;
    private final EventRepository eventRepository;

    public CouponService(CouponRepository repository, EventRepository eventRepository) {
        this.repository = repository;
        this.eventRepository = eventRepository;
    }

    public Coupon addCouponToEvent(UUID eventId, CouponRequestDTO data) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Coupon not found"));

        Coupon newCoupon = new Coupon();

        newCoupon.setCode(data.code());
        newCoupon.setDiscount(data.discount());
        newCoupon.setEvent(event);
        newCoupon.setValidUntil(data.validUntil());

        return repository.save(newCoupon);
    }

    public List<Coupon> getCouponsByEvent(UUID eventId, OffsetDateTime currentDate) {
        return repository.findByIdAndValidAfter(eventId, currentDate);
    }
}
