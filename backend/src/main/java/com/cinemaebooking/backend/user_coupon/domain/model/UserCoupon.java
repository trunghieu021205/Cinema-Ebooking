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
     * Factory method: tạo UserCoupon mới khi redeem.
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

    // ========== BUSINESS METHODS ==========

    /**
     * Sử dụng coupon (giảm 1 lần dùng).
     */
    public void use() {
        if (this.status != UserCouponStatus.AVAILABLE) {
            throw CommonExceptions.invalidInput(
                    "Cannot use coupon: status must be AVAILABLE (current: " + this.status + ")");
        }
        if (this.usageRemain <= 0) {
            throw CommonExceptions.invalidInput("Cannot use coupon: no remaining usage");
        }
        this.usageRemain--;
        if (this.usageRemain == 0) {
            this.status = UserCouponStatus.USED;
            this.usedAt = LocalDateTime.now();
        }
    }

    /**
     * Hoàn tác một lần sử dụng (khi huỷ booking). Tăng usageRemain, nếu là USED và hết lượt thì quay về AVAILABLE nếu chưa hết hạn.
     */
    public void restoreUsage(LocalDateTime now) {
        if (this.status == UserCouponStatus.EXPIRED) {
            throw CommonExceptions.invalidInput("Cannot restore an already expired coupon");
        }
        if (this.usageRemain == 0 && this.status == UserCouponStatus.USED) {
            if (this.expiredAt != null && this.expiredAt.isBefore(now)) {
                // Đã hết hạn → không thể khôi phục
                throw CommonExceptions.invalidInput(
                        "Cannot restore coupon: it has passed its expiry date");
            }
            this.status = UserCouponStatus.AVAILABLE;
        }
        this.usageRemain++;
    }

    /**
     * Đánh dấu coupon đã hết hạn.
     */
    public void expire() {
        if (this.status == UserCouponStatus.AVAILABLE) {
            this.status = UserCouponStatus.EXPIRED;
        }
    }

    /**
     * Admin thu hồi coupon (đặt trạng thái EXPIRED).
     */
    public void revoke() {
        if (this.status == UserCouponStatus.EXPIRED) {
            throw CommonExceptions.invalidInput("Cannot revoke coupon: already EXPIRED");
        }
        this.status = UserCouponStatus.EXPIRED;
    }

    // Phương thức cũ giữ lại (không dùng trong use case mới)
    @Deprecated
    public void markAsUsed() {
        use();
    }
}