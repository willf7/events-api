package com.events.api.domain.coupon;

import com.events.api.domain.event.Event;
import com.events.api.domain.event.EventDetailsDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CouponMapper {
    @Mapping(target = "eventIds", expression = "java(toEventIds(coupon.getEvents()))")
    CouponResponseDTO toResponseDTO(Coupon coupon);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "events", ignore = true)
    @Mapping(target = "usages", ignore = true)
    @Mapping(target = "usesCount", constant = "0")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Coupon toEntity(CouponRequestDTO request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "events", ignore = true)
    @Mapping(target = "usages", ignore = true)
    @Mapping(target = "usesCount", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(@MappingTarget Coupon coupon, CouponUpdateRequestDTO request);

    EventDetailsDTO.CouponDTO toEventCouponDTO(Coupon coupon);

    default Set<UUID> toEventIds(Set<Event> events) {
        if (events == null) {
            return Set.of();
        }

        return events.stream()
                .map(Event::getId)
                .collect(Collectors.toSet());
    }
}
