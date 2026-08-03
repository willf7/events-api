package com.events.api.controller;

import com.events.api.domain.coupon.Coupon;
import com.events.api.domain.coupon.CouponRequestDTO;
import com.events.api.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/coupon")
public class CouponController {
    @Autowired
    private CouponService couponService;

    @PostMapping("event/{eventId}")
    public ResponseEntity<Coupon> create(@PathVariable UUID eventId, @RequestBody CouponRequestDTO body) {
        Coupon coupon = couponService.addCouponToEvent(eventId, body);
        return ResponseEntity.ok(coupon);
    }
}
