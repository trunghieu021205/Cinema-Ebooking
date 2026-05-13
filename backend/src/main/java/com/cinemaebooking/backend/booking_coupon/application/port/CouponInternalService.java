package com.cinemaebooking.backend.booking_coupon.application.port;

import com.cinemaebooking.backend.booking_coupon.domain.model.BookingCoupon;

import java.math.BigDecimal;

public interface CouponInternalService {
    BookingCoupon validateAndGetCoupon(Long userId, String couponCode, BigDecimal totalAmount);
}
