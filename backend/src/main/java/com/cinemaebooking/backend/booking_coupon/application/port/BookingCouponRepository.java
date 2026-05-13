package com.cinemaebooking.backend.booking_coupon.application.port;

import com.cinemaebooking.backend.booking_coupon.domain.model.BookingCoupon;
import java.util.Optional;

public interface BookingCouponRepository {
    BookingCoupon save(BookingCoupon bookingCoupon);
    Optional<BookingCoupon> findByBookingId(Long bookingId);
    public void delete(BookingCoupon domain);
}