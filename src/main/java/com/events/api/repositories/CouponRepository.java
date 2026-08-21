package com.events.api.repositories;

import com.events.api.domain.coupon.Coupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public interface CouponRepository extends JpaRepository<Coupon, UUID> {
    @Query("SELECT c FROM Coupon c JOIN c.events e " +
            "WHERE e.id = :eventId " +
            "AND c.active = true " +
            "AND c.validUntil >= :currentDate " +
            "AND (c.maxUses IS NULL OR c.usesCount < c.maxUses)")
    List<Coupon> findByEventIdAndValidAfter(UUID eventId, OffsetDateTime currentDate);

    @Query("SELECT DISTINCT c FROM Coupon c LEFT JOIN c.events e " +
            "WHERE (:eventId IS NULL OR e.id = :eventId) " +
            "AND (:code IS NULL OR LOWER(c.code) LIKE CONCAT('%', LOWER(:code), '%')) " +
            "AND (:active IS NULL OR c.active = :active) " +
            "AND (:valid IS NULL " +
            "OR (:valid = true AND c.active = true AND c.validUntil >= :currentDate AND (c.maxUses IS NULL OR c.usesCount < c.maxUses)) " +
            "OR (:valid = false AND (c.active = false OR c.validUntil < :currentDate OR (c.maxUses IS NOT NULL AND c.usesCount >= c.maxUses))))")
    Page<Coupon> findFilteredCoupons(@Param("eventId") UUID eventId,
                                     @Param("code") String code,
                                     @Param("active") Boolean active,
                                     @Param("valid") Boolean valid,
                                     @Param("currentDate") OffsetDateTime currentDate,
                                     Pageable pageable);
}
