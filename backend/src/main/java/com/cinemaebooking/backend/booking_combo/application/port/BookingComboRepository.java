package com.cinemaebooking.backend.booking_combo.application.port;

import com.cinemaebooking.backend.booking_combo.domain.model.BookingCombo;
import java.util.List;
import java.util.Optional;

public interface BookingComboRepository {
    BookingCombo save(BookingCombo bookingCombo);
    List<BookingCombo> findByBookingId(Long bookingId);
    Optional<BookingCombo> findById(Long id);
}
