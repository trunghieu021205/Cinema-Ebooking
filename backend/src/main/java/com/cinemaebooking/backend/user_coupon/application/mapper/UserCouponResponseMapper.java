package com.cinemaebooking.backend.user_coupon.application.mapper;

import com.cinemaebooking.backend.user_coupon.application.dto.UserCouponResponse;
import com.cinemaebooking.backend.user_coupon.domain.model.UserCoupon;
import org.springframework.stereotype.Component;

@Component
public class UserCouponResponseMapper {

    public UserCouponResponse toResponse(UserCoupon userCoupon) {
        if (userCoupon == null) return null;
        return new UserCouponResponse(
                userCoupon.getId() != null ? userCoupon.getId().getValue() : null,
                userCoupon.getUserId(),
                userCoupon.getCouponId(),
                userCoupon.getReceivedAt(),
                userCoupon.getUsageRemain(),
                userCoupon.getUsedAt(),
                userCoupon.getExpiredAt(),
                userCoupon.getStatus()
        );
    }
}