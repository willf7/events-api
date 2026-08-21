package com.events.api.coupon.controller;

import com.events.api.common.dto.PageResponseDTO;
import com.events.api.coupon.dto.CouponRequestDTO;
import com.events.api.coupon.dto.CouponResponseDTO;
import com.events.api.coupon.dto.CouponSearchRequestDTO;
import com.events.api.coupon.dto.CouponUpdateRequestDTO;
import com.events.api.security.JWTUserData;
import com.events.api.coupon.service.CouponService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Validated
@RestController
@RequestMapping("/api/coupon")
public class CouponController {
    private final CouponService couponService;

    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @PostMapping
    public ResponseEntity<CouponResponseDTO> create(@Valid @RequestBody CouponRequestDTO request) {
        JWTUserData userData = (JWTUserData) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CouponResponseDTO coupon = couponService.create(request, userData);
        return ResponseEntity.status(201).body(coupon);
    }

    @GetMapping
    public ResponseEntity<PageResponseDTO<CouponResponseDTO>> findAll(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(50) int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @ModelAttribute CouponSearchRequestDTO request
    ) {
        PageResponseDTO<CouponResponseDTO> response = couponService.findAll(page, size, sortBy, sortDirection, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{couponId}")
    public ResponseEntity<CouponResponseDTO> findById(@PathVariable UUID couponId) {
        CouponResponseDTO coupon = couponService.findById(couponId);
        return ResponseEntity.ok(coupon);
    }

    @PreAuthorize("hasRole('ADMIN') or @couponAuth.canModify(#couponId)")
    @PutMapping("/{couponId}")
    public ResponseEntity<CouponResponseDTO> update(@PathVariable UUID couponId, @Valid @RequestBody CouponUpdateRequestDTO request) {
        CouponResponseDTO coupon = couponService.update(couponId, request);
        return ResponseEntity.ok(coupon);
    }

    @PreAuthorize("hasRole('ADMIN') or @couponAuth.canModify(#couponId)")
    @DeleteMapping("/{couponId}")
    public ResponseEntity<Void> delete(@PathVariable UUID couponId) {
        couponService.delete(couponId);
        return ResponseEntity.noContent().build();
    }
}
