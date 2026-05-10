package com.cinemaebooking.backend.user_coupon.application.port;

import java.time.LocalDateTime;
import java.util.Optional;

public interface CouponPort {

    Optional<CouponSnapshot> findValidCoupon(Long couponId, LocalDateTime now);

    record CouponSnapshot(Long id, boolean active, LocalDateTime expiryDate,
                          int pointsToRedeem, int perUserUsage) {}
}