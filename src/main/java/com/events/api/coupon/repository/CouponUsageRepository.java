package com.events.api.coupon.repository;

import com.events.api.coupon.model.CouponUsage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CouponUsageRepository extends JpaRepository<CouponUsage, UUID> {
}
