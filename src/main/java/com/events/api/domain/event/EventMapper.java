package com.events.api.domain.event;

import com.events.api.domain.address.Address;
import com.events.api.domain.coupon.Coupon;
import com.events.api.domain.coupon.CouponMapper;
import com.events.api.domain.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = CouponMapper.class)
public interface EventMapper {
    @Mapping(target = "city", source = "address.city")
    @Mapping(target = "state", source = "address.state")
    EventResponseDTO toResponseDTO(Event event);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner", source = "owner")
    @Mapping(target = "imgUrl", source = "imgUrl")
    @Mapping(target = "address", source = "address")
    @Mapping(target = "coupons", ignore = true)
    Event toEntity(EventRequestDTO request, User owner, String imgUrl, Address address);

    @Mapping(target = "city", source = "event.address.city")
    @Mapping(target = "state", source = "event.address.state")
    @Mapping(target = "coupons", source = "coupons")
    EventDetailsDTO toDetailsDTO(Event event, List<Coupon> coupons);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "imgUrl", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "address", source = "address")
    @Mapping(target = "coupons", ignore = true)
    void updateEntity(@MappingTarget Event event, EventUpdateRequestDTO request, Address address);
}
