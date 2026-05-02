package com.cinemaebooking.backend.common.exception.domain;

import com.cinemaebooking.backend.common.exception.BaseException;
import com.cinemaebooking.backend.common.exception.ErrorCode;
import com.cinemaebooking.backend.user_coupon.domain.valueobject.UserCouponId;

public final class UserCouponExceptions {

    private UserCouponExceptions() {}

    public static BaseException notFound(UserCouponId id) {
        return new BaseException(ErrorCode.USER_COUPON_NOT_FOUND,
                "User coupon not found with id: " + id);
    }

    public static BaseException alreadyExists(Long userId, Long couponId) {
        return new BaseException(ErrorCode.USER_COUPON_ALREADY_EXISTS,
                "User already has this coupon: userId=" + userId + ", couponId=" + couponId);
    }

    public static BaseException couponNotFound(Long couponId) {
        return new BaseException(ErrorCode.COUPON_NOT_FOUND,
                "Coupon not found: " + couponId);
    }

    public static BaseException couponNotActive(Long couponId) {
        return new BaseException(ErrorCode.COUPON_INVALID,
                "Coupon is not active: " + couponId);
    }

    public static BaseException couponExpired(Long couponId) {
        return new BaseException(ErrorCode.COUPON_EXPIRED,
                "Coupon has expired: " + couponId);
    }

    public static BaseException insufficientPoints(Long userId, int required, int available) {
        return new BaseException(ErrorCode.LOYALTY_INSUFFICIENT_POINTS,
                "User " + userId + " has insufficient points (required: " + required + ", available: " + available + ")");
    }
}