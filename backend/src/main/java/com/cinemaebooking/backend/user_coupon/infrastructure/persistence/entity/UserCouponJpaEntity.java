package com.cinemaebooking.backend.user_coupon.infrastructure.persistence.entity;

import com.cinemaebooking.backend.infrastructure.persistence.entity.BaseJpaEntity;
import com.cinemaebooking.backend.user_coupon.domain.enums.UserCouponStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * UserCouponJpaEntity - Persistence model for the user_coupon join table.
 * Responsibility:
 * - Map database table user_coupons
 * - Track which users have claimed which coupons and their usage status
 * - Maintain loose coupling with User and Coupon entities (only stores IDs)
 * Note:
 * - This is NOT a domain model
 * - Must be converted via Mapper
 * - No business logic allowed inside this entity
 *
 * @author Hieu Nguyen
 * @since 2026
 */
@Entity
@Table(name = "user_coupons")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class UserCouponJpaEntity extends BaseJpaEntity {

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "coupon_id", nullable = false)
    private Long couponId;

    @Column(name = "received_at", nullable = false)
    private LocalDateTime receivedAt;

    /**
     * This value is copied from Coupon.perUserUsage when the user claims the coupon.
     * Must always be >= 0.
     */
    @Column(name = "usage_remain", nullable = false)
    private Integer usageRemain;

    /**
     * Should be null if status is AVAILABLE.
     */
    @Column(name = "used_at")
    private LocalDateTime usedAt;

    /**
     * Stored independently (derived from Coupon.endDate at claim time).
     */
    @Column(name = "expired_at", nullable = false)
    private LocalDateTime expiredAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserCouponStatus status;

}
