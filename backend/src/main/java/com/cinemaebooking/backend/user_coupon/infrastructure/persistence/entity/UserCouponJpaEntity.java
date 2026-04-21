package com.cinemaebooking.backend.user_coupon.infrastructure.persistence.entity;

import com.cinemaebooking.backend.infrastructure.persistence.entity.BaseJpaEntity;
import com.cinemaebooking.backend.user_coupon.domain.enums.UserCouponStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * UserCouponJpaEntity - Persistence model for user_coupons table.
 * Responsibility:
 * - Map database table user_coupons
 * - Handle persistence concerns only
 * - No business logic allowed
 * Note:
 * - This is NOT a domain model
 * - Must be converted via Mapper
 *
 * @author Hieu Nguyen
 * @since 2026
 */
@Entity
@Table(
        name = "user_coupons",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_user_coupons_user_id_coupon_id_deleted",
                        columnNames = {"user_id", "coupon_id", "deleted"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class UserCouponJpaEntity extends BaseJpaEntity {

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "coupon_id", nullable = false)
    private Long couponId;

    @Column(name = "received_at", nullable = false)
    private LocalDateTime receivedAt;

    @PositiveOrZero
    @Column(name = "usage_remain", nullable = false)
    private Integer usageRemain;

    @Column(name = "used_at")
    private LocalDateTime usedAt;

    @Column(name = "expired_at", nullable = false)
    private LocalDateTime expiredAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserCouponStatus status;
}