package com.cinemaebooking.backend.booking_coupon.presentation;

import com.cinemaebooking.backend.booking_coupon.application.dto.BookingCouponResponse;
import com.cinemaebooking.backend.booking_coupon.application.usecase.GetBookingCouponByBookingUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/booking-coupons")
@RequiredArgsConstructor
public class BookingCouponController {

    private final GetBookingCouponByBookingUseCase getBookingCouponByBookingUseCase;

    @GetMapping("/booking/{bookingId}")
    public BookingCouponResponse getByBooking(@PathVariable Long bookingId) {
        return getBookingCouponByBookingUseCase.execute(bookingId);
    }
}
