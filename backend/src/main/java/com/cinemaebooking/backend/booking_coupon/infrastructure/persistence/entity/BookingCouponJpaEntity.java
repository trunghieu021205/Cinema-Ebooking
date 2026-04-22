package com.cinemaebooking.backend.booking_coupon.infrastructure.persistence.entity;

import com.cinemaebooking.backend.booking.infrastructure.persistence.entity.BookingJpaEntity;
import com.cinemaebooking.backend.infrastructure.persistence.entity.BaseJpaEntity;
import com.cinemaebooking.backend.user_coupon.infrastructure.persistence.entity.UserCouponJpaEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * BookingCouponJpaEntity - Persistence model for booking_coupons table.
 * Responsibility:
 * - Map database table booking_coupons
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
        name = "booking_coupons",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_booking_coupons_booking_id_user_coupon_id_deleted",
                        columnNames = {"booking_id", "user_coupon_id", "deleted"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class BookingCouponJpaEntity extends BaseJpaEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private BookingJpaEntity booking;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_coupon_id", nullable = false)
    private UserCouponJpaEntity userCoupon;

    @Column(name = "coupon_code", nullable = false, length = 50)
    private String couponCode;

    @PositiveOrZero
    @Column(name = "discount_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal discountAmount;

    @Column(name = "applied_at", nullable = false)
    private LocalDateTime appliedAt;
}