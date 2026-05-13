package com.cinemaebooking.backend.booking_coupon.domain.valueObject;

import com.cinemaebooking.backend.common.domain.BaseId;

public final class BookingCouponId extends BaseId {
    private BookingCouponId(Long value) {
        super(value);
    }

    public static BookingCouponId of(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("Booking Coupon Id must be positive");
        }
        return new BookingCouponId(value);
    }

    public static BookingCouponId ofNullable(Long value) {
        return value == null ? null : new BookingCouponId(value);
    }
}
