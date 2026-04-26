package com.cinemaebooking.backend.coupon.domain.valueobject;

import com.cinemaebooking.backend.common.domain.BaseId;

public final class CouponId extends BaseId {

    private CouponId(Long value) {
        super(value);
    }

    public static CouponId of(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("CouponId value must be positive number");
        }
        return new CouponId(value);
    }

    public static CouponId ofNullable(Long value) {
        return value == null ? null : new CouponId(value);
    }
}