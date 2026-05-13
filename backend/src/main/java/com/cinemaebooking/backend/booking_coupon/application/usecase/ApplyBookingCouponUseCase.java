package com.cinemaebooking.backend.booking_coupon.application.usecase;

import com.cinemaebooking.backend.booking_coupon.application.dto.ApplyBookingCouponRequest;
import com.cinemaebooking.backend.booking_coupon.application.port.BookingCouponRepository;
import com.cinemaebooking.backend.booking_coupon.domain.model.BookingCoupon;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ApplyBookingCouponUseCase {

    private final BookingCouponRepository bookingCouponRepository;

    @Transactional
    public void execute(Long bookingId, ApplyBookingCouponRequest request) {
        if (request == null) return;

        BookingCoupon bookingCoupon = BookingCoupon.builder()
                .bookingId(bookingId)
                .userCouponId(request.getUserCouponId())
                .code(request.getCode())
                .discountValue(request.getDiscountValue())
                .appliedAt(LocalDateTime.now())
                .build();

        bookingCoupon.validate(); // Business validation trong domain model
        bookingCouponRepository.save(bookingCoupon);
    }
}