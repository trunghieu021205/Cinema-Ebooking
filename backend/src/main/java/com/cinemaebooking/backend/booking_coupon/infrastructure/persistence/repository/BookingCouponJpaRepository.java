package com.cinemaebooking.backend.booking_coupon.infrastructure.persistence.repository;

import com.cinemaebooking.backend.booking_coupon.infrastructure.persistence.entity.BookingCouponJpaEntity;
import com.cinemaebooking.backend.infrastructure.persistence.repository.SoftDeleteJpaRepository;
import java.util.Optional;

public interface BookingCouponJpaRepository extends SoftDeleteJpaRepository<BookingCouponJpaEntity> {
    Optional<BookingCouponJpaEntity> findByBookingIdAndDeletedAtIsNull(Long bookingId);
}
