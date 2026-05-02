package com.cinemaebooking.backend.user_coupon.domain.valueobject;

import com.cinemaebooking.backend.common.domain.BaseId;
import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;

public final class UserCouponId extends BaseId {

    private UserCouponId(Long value) { super(value); }

    public static UserCouponId of(Long value) {
        if (value == null || value <= 0) {
            throw CommonExceptions.invalidInput("UserCouponId value must be positive");
        }
        return new UserCouponId(value);
    }

    public static UserCouponId ofNullable(Long value) {
        return value == null ? null : new UserCouponId(value);
    }
}