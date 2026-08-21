package com.events.api.coupon.service;

import com.events.api.common.dto.PageResponseDTO;
import com.events.api.coupon.model.Coupon;
import com.events.api.coupon.mapper.CouponMapper;
import com.events.api.coupon.dto.CouponRequestDTO;
import com.events.api.coupon.dto.CouponResponseDTO;
import com.events.api.coupon.dto.CouponSearchRequestDTO;
import com.events.api.coupon.dto.CouponUpdateRequestDTO;
import com.events.api.event.model.Event;
import com.events.api.exceptions.EntityNotFoundException;
import com.events.api.coupon.repository.CouponRepository;
import com.events.api.event.repository.EventRepository;
import com.events.api.security.JWTUserData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@Service
public class CouponService {
    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of("createdAt", "usesCount", "discount");

    private final CouponRepository couponRepository;
    private final EventRepository eventRepository;
    private final CouponMapper couponMapper;

    public CouponService(CouponRepository couponRepository, EventRepository eventRepository, CouponMapper couponMapper) {
        this.couponRepository = couponRepository;
        this.eventRepository = eventRepository;
        this.couponMapper = couponMapper;
    }

    @Transactional
    public CouponResponseDTO create(CouponRequestDTO request, JWTUserData userData) {
        List<Event> events = eventRepository.findByIdInAndOwnerId(request.eventIds(), userData.userId());

        if (events.size() != request.eventIds().size()) {
            throw new AccessDeniedException("Coupon can only be linked to events owned by the current user");
        }

        Coupon newCoupon = couponMapper.toEntity(request);
        newCoupon.getEvents().addAll(events);

        Coupon savedCoupon = couponRepository.save(newCoupon);

        return couponMapper.toResponseDTO(savedCoupon);
    }

    @Transactional(readOnly = true)
    public PageResponseDTO<CouponResponseDTO> findAll(int page, int size, String sortBy, String sortDirection, CouponSearchRequestDTO request) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(resolveDirection(sortDirection), resolveSortBy(sortBy)));
        String code = blankToNull(request.code());
        OffsetDateTime currentDate = request.valid() != null ? OffsetDateTime.now() : null;
        Page<Coupon> coupons = couponRepository.findFilteredCoupons(request.eventId(), code, request.active(), request.valid(), currentDate, pageable);

        return PageResponseDTO.from(coupons.map(couponMapper::toResponseDTO));
    }

    @Transactional(readOnly = true)
    public CouponResponseDTO findById(UUID couponId) {
        Coupon coupon = this.getCouponOrThrow(couponId);

        return couponMapper.toResponseDTO(coupon);
    }

    @Transactional
    public CouponResponseDTO update(UUID couponId, CouponUpdateRequestDTO request) {
        Coupon coupon = this.getCouponOrThrow(couponId);

        couponMapper.updateEntity(coupon, request);
        Coupon savedCoupon = couponRepository.save(coupon);

        return couponMapper.toResponseDTO(savedCoupon);
    }

    @Transactional
    public void delete(UUID couponId) {
        Coupon coupon = this.getCouponOrThrow(couponId);

        couponRepository.delete(coupon);
    }

    @Transactional(readOnly = true)
    public List<Coupon> getCouponsByEvent(UUID eventId, OffsetDateTime currentDate) {
        return couponRepository.findByEventIdAndValidAfter(eventId, currentDate);
    }

    private Coupon getCouponOrThrow(UUID couponId) {
        return couponRepository.findById(couponId)
                .orElseThrow(() -> new EntityNotFoundException("Coupon not found"));
    }

    private String blankToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }

    private String resolveSortBy(String sortBy) {
        if (ALLOWED_SORT_FIELDS.contains(sortBy)) {
            return sortBy;
        }

        return "createdAt";
    }

    private Sort.Direction resolveDirection(String sortDirection) {
        if (sortDirection == null) {
            return Sort.Direction.DESC;
        }

        try {
            return Sort.Direction.valueOf(sortDirection.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException exception) {
            return Sort.Direction.DESC;
        }
    }
}
