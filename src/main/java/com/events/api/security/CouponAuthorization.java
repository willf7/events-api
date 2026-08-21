package com.events.api.security;

import com.events.api.domain.user.Role;
import com.events.api.repositories.CouponRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component("couponAuth")
public class CouponAuthorization {
    private final CouponRepository couponRepository;

    public CouponAuthorization(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    public boolean canModify(UUID couponId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !(auth.getPrincipal() instanceof JWTUserData user)) {
            return false;
        }

        if (user.roles().contains(Role.ROLE_ADMIN.toString())) {
            return true;
        }

        return couponRepository.findById(couponId)
                .map(coupon -> coupon.getEvents().stream()
                        .anyMatch(event -> event.getOwner() != null && event.getOwner().getId().equals(user.userId())))
                .orElse(false);
    }
}
