package com.events.api.domain.event;

import com.events.api.domain.address.Address;
import com.events.api.domain.coupon.Coupon;
import com.events.api.domain.user.User;

import java.util.List;

public final class EventMapper {
    private EventMapper() {
    }

    public static EventResponseDTO toResponseDTO(Event event) {
        return new EventResponseDTO(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getEventUrl(),
                event.getRemote(),
                event.getAddress() != null ? event.getAddress().getCity() : "",
                event.getAddress() != null ? event.getAddress().getState() : "",
                event.getDate(),
                event.getImgUrl()
        );
    }

    public static Event toEntity(EventRequestDTO request, User owner, String imgUrl, Address address) {
        Event event = new Event();
        event.setTitle(request.title());
        event.setDescription(request.description());
        event.setEventUrl(request.eventUrl());
        event.setImgUrl(imgUrl);
        event.setRemote(request.remote());
        event.setDate(request.date());
        event.setOwner(owner);
        event.setAddress(address);

        return event;
    }

    public static EventDetailsDTO toDetailsDTO(Event event, List<Coupon> coupons) {
        List<EventDetailsDTO.CouponDTO> couponDTOS = coupons.stream()
                .map(coupon -> new EventDetailsDTO.CouponDTO(
                        coupon.getCode(),
                        coupon.getDiscount(),
                        coupon.getValidUntil()
                ))
                .toList();

        return new EventDetailsDTO(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getEventUrl(),
                event.getRemote(),
                event.getAddress() != null ? event.getAddress().getCity() : "",
                event.getAddress() != null ? event.getAddress().getState() : "",
                event.getDate(),
                event.getImgUrl(),
                couponDTOS
        );
    }

    public static void updateEntity(Event event, EventUpdateRequestDTO request, String imgUrl, Address address) {
        event.setTitle(request.title());
        event.setDescription(request.description());
        event.setEventUrl(request.eventUrl());
        event.setRemote(request.remote());
        event.setDate(request.date());
        event.setAddress(address);

        if (imgUrl != null) {
            event.setImgUrl(imgUrl);
        }
    }
}
