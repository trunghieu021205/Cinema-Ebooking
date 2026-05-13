package com.cinemaebooking.backend.booking_combo.application.port;

import com.cinemaebooking.backend.booking.application.dto.CreateBookingRequest;
import com.cinemaebooking.backend.booking_combo.domain.model.BookingCombo;

import java.util.List;

public interface ComboInternalService {
    List<BookingCombo> getBookingCombos(List<CreateBookingRequest.ComboSelectionItem> selections);
}
