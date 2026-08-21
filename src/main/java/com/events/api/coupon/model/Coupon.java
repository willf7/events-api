package com.events.api.coupon.model;

import com.events.api.event.model.Event;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Table(name = "coupon")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Coupon {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private Integer discount;

    @Column(nullable = false, length = 100)
    private String code;

    @Column(nullable = false)
    private OffsetDateTime validUntil;

    @Column(nullable = false)
    private Boolean active = true;

    @Column(nullable = false)
    private Boolean singleUsePerUser = false;

    private Integer maxUses;

    @Column(nullable = false)
    private Integer usesCount = 0;

    @Column(nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(nullable = false)
    private OffsetDateTime updatedAt;

    @ManyToMany
    @JoinTable(
            name = "coupon_events",
            joinColumns = @JoinColumn(name = "coupon_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    private Set<Event> events = new HashSet<>();

    @OneToMany(mappedBy = "coupon")
    private Set<CouponUsage> usages = new HashSet<>();

    @PrePersist
    public void prePersist() {
        OffsetDateTime now = OffsetDateTime.now();
        createdAt = now;
        updatedAt = now;

        if (active == null) {
            active = true;
        }

        if (singleUsePerUser == null) {
            singleUsePerUser = false;
        }

        if (usesCount == null) {
            usesCount = 0;
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = OffsetDateTime.now();
    }
}
