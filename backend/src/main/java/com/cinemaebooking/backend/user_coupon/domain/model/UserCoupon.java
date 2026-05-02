package com.cinemaebooking.backend.user_coupon.domain.model;

import com.cinemaebooking.backend.common.domain.BaseEntity;
import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.user_coupon.domain.enums.UserCouponStatus;
import com.cinemaebooking.backend.user_coupon.domain.valueobject.UserCouponId;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
public class UserCoupon extends BaseEntity<UserCouponId> {

    private Long userId;
    private Long couponId;
    private LocalDateTime receivedAt;
    private Integer usageRemain;
    private LocalDateTime usedAt;
    private LocalDateTime expiredAt;
    private UserCouponStatus status;

    /**
     * Factory method for redeeming a new coupon.
     */
    public static UserCoupon redeem(Long userId, Long couponId,
                                    LocalDateTime receivedAt,
                                    Integer usageRemain,
                                    LocalDateTime expiredAt) {
        return UserCoupon.builder()
                .userId(userId)
                .couponId(couponId)
                .receivedAt(receivedAt)
                .usageRemain(usageRemain)
                .usedAt(null)
                .expiredAt(expiredAt)
                .status(UserCouponStatus.AVAILABLE)
                .build();
    }

    /**
     * Mark coupon as used (decrease usageRemain, update status if exhausted).
     */
    public void markAsUsed() {
        if (this.status != UserCouponStatus.AVAILABLE) {
            throw CommonExceptions.invalidInput(
                    "Cannot mark as used: coupon is not AVAILABLE (current: " + this.status + ")");
        }
        this.usageRemain = this.usageRemain - 1;
        if (this.usageRemain <= 0) {
            this.status = UserCouponStatus.USED;
            this.usedAt = LocalDateTime.now();
        }
    }

    public void expire() {
        if (this.status == UserCouponStatus.AVAILABLE) {
            this.status = UserCouponStatus.EXPIRED;
        }
    }
}