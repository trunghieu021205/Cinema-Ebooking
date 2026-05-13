package com.cinemaebooking.backend.booking_combo.application.mapper;

import com.cinemaebooking.backend.booking_combo.application.dto.BookingComboResponse;
import com.cinemaebooking.backend.booking_combo.domain.model.BookingCombo;
import org.springframework.stereotype.Component;

@Component
public class BookingComboResponseMapper {
    public BookingComboResponse toResponse(BookingCombo domain) {
        if (domain == null) return null;
        return BookingComboResponse.builder()
                .id(domain.getId() != null ? domain.getId().getValue() : null)
                .bookingId(domain.getBookingId())
                .comboId(domain.getComboId())
                .comboName(domain.getComboName())
                .unitPrice(domain.getUnitPrice())
                .quantity(domain.getQuantity())
                .totalPrice(domain.getTotalPrice())
                .build();
    }
}
