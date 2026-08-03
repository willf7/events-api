package com.events.api.repositories;

import com.events.api.domain.coupon.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface CouponRepository extends JpaRepository<Coupon, UUID> {
    @Query("SELECT c FROM Coupon c WHERE c.event.id = :eventId AND c.valid >= :currentDate")
    List<Coupon> findByIdAndValidAfter(UUID eventId, Date currentDate);
}
