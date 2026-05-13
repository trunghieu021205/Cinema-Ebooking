package com.cinemaebooking.backend.booking_combo.infrastructure.persistence.repository;

import com.cinemaebooking.backend.booking_combo.infrastructure.persistence.entity.BookingComboJpaEntity;
import com.cinemaebooking.backend.infrastructure.persistence.repository.SoftDeleteJpaRepository;
import java.util.List;

public interface BookingComboJpaRepository extends SoftDeleteJpaRepository<BookingComboJpaEntity> {
    List<BookingComboJpaEntity> findAllByBookingIdAndDeletedAtIsNull(Long bookingId);
}
