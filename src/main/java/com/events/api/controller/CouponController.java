package com.events.api.controller;

import com.events.api.domain.coupon.CouponRequestDTO;
import com.events.api.domain.coupon.CouponResponseDTO;
import com.events.api.service.CouponService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/coupon")
public class CouponController {
    private final CouponService couponService;

    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @PreAuthorize("hasRole('ADMIN') or @eventAuth.canModify(#eventId)")
    @PostMapping("event/{eventId}")
    public ResponseEntity<CouponResponseDTO> create(@PathVariable UUID eventId, @Valid @RequestBody CouponRequestDTO body) {
        CouponResponseDTO coupon = couponService.addCouponToEvent(eventId, body);
        return ResponseEntity.ok(coupon);
    }
}
