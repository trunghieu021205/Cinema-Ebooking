package com.cinemaebooking.backend.booking_combo.domain.valueObject;

import com.cinemaebooking.backend.common.domain.BaseId;

public final class BookingComboId extends BaseId {
    private BookingComboId(Long value) {
        super(value);
    }

    public static BookingComboId of(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("Booking Combo Id must be positive");
        }
        return new BookingComboId(value);
    }

    public static BookingComboId ofNullable(Long value) {
        return value == null ? null : new BookingComboId(value);
    }
}
