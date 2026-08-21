package com.events.api.domain.coupon;

import com.events.api.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Table(name = "coupon_usage")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CouponUsage {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id", nullable = false)
    private Coupon coupon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 100)
    private String codeSnapshot;

    @Column(nullable = false)
    private Integer discountSnapshot;

    @Column(nullable = false)
    private OffsetDateTime validUntilSnapshot;

    @Column(nullable = false)
    private OffsetDateTime usedAt;

    @PrePersist
    public void prePersist() {
        if (usedAt == null) {
            usedAt = OffsetDateTime.now();
        }
    }
}
