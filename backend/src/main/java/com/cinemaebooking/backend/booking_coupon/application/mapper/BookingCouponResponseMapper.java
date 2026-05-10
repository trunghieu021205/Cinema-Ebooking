package com.cinemaebooking.backend.booking_coupon.application.mapper;

import com.cinemaebooking.backend.booking_coupon.application.dto.BookingCouponResponse;
import com.cinemaebooking.backend.booking_coupon.domain.model.BookingCoupon;
import org.springframework.stereotype.Component;

@Component
public class BookingCouponResponseMapper {
    public BookingCouponResponse toResponse(BookingCoupon domain) {
        if (domain == null) return null;
        return BookingCouponResponse.builder()
                .id(domain.getId() != null ? domain.getId().getValue() : null)
                .bookingId(domain.getBookingId())
                .userCouponId(domain.getUserCouponId())
                .code(domain.getCode())
                .discountValue(domain.getDiscountValue())
                .appliedAt(domain.getAppliedAt())
                .build();
    }
}
