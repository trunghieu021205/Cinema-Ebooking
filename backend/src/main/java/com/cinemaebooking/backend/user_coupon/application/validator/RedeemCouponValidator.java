package com.cinemaebooking.backend.user_coupon.application.validator;

import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.common.exception.domain.UserCouponExceptions;
import com.cinemaebooking.backend.user_coupon.application.dto.RedeemCouponRequest;
import com.cinemaebooking.backend.user_coupon.application.port.CouponPort;
import com.cinemaebooking.backend.user_coupon.application.port.LoyaltyPort;
import com.cinemaebooking.backend.user_coupon.application.port.UserCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class RedeemCouponValidator {

    private final UserCouponRepository userCouponRepository;
    private final CouponPort couponPort;
    private final LoyaltyPort loyaltyPort;

    public void validate(RedeemCouponRequest request) {
        if (request == null) {
            throw CommonExceptions.invalidInput("Redeem request must not be null");
        }
        Long userId = request.getUserId();
        Long couponId = request.getCouponId();

        if (userId == null || couponId == null) {
            throw CommonExceptions.invalidInput("userId and couponId must not be null");
        }

        LocalDateTime now = LocalDateTime.now();
        var coupon = couponPort.findValidCoupon(couponId, now)
                .orElseThrow(() -> UserCouponExceptions.couponNotFound(couponId));

        if (!coupon.active()) {
            throw UserCouponExceptions.couponNotActive(couponId);
        }
        if (coupon.expiryDate() != null && coupon.expiryDate().isBefore(now)) {
            throw UserCouponExceptions.couponExpired(couponId);
        }

        int pointsRequired = coupon.pointsToRedeem();
        if (pointsRequired > 0) {
            int userPoints = loyaltyPort.getUserPoints(userId);
            if (userPoints < pointsRequired) {
                throw UserCouponExceptions.insufficientPoints(userId, pointsRequired, userPoints);
            }
        }

        if (userCouponRepository.existsByUserIdAndCouponId(userId, couponId)) {
            throw UserCouponExceptions.alreadyExists(userId, couponId);
        }
    }
}