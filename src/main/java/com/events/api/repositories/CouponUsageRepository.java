package com.events.api.repositories;

import com.events.api.domain.coupon.CouponUsage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CouponUsageRepository extends JpaRepository<CouponUsage, UUID> {
}
