package com.cinemaebooking.backend.common.exception.domain;

import com.cinemaebooking.backend.common.exception.BaseException;
import com.cinemaebooking.backend.common.exception.ErrorCode;
import com.cinemaebooking.backend.coupon.domain.valueobject.CouponId;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CouponExceptions {

    public static BaseException notFound(CouponId id) {
        return new BaseException(ErrorCode.COUPON_NOT_FOUND,
                "Coupon not found with id: " + id);
    }

    public static BaseException notFound(String code) {
        return new BaseException(ErrorCode.COUPON_NOT_FOUND,
                "Coupon not found with code: " + code);
    }

    public static BaseException alreadyExists(String code) {
        return new BaseException(ErrorCode.COUPON_ALREADY_EXISTS,
                "Coupon with code '" + code + "' already exists");
    }

    public static BaseException expired(String code) {
        return new BaseException(ErrorCode.COUPON_EXPIRED,
                "Coupon '" + code + "' has expired");
    }

    public static BaseException invalidForBooking() {
        return new BaseException(ErrorCode.COUPON_INVALID,
                "Coupon is not valid for this booking");
    }

    public static BaseException maxUsageReached() {
        return new BaseException(ErrorCode.COUPON_MAX_USAGE_REACHED,
                "Coupon has reached its maximum usage limit");
    }
}