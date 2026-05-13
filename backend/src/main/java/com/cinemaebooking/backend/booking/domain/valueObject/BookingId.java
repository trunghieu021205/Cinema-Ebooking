package com.cinemaebooking.backend.booking.domain.valueObject;

import com.cinemaebooking.backend.common.domain.BaseId;

public final class BookingId extends BaseId {
    private BookingId(Long value) {
        super(value);
    }

    public static BookingId of(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("BookingId must be positive");
        }
        return new BookingId(value);
    }

    public static BookingId ofNullable(Long value) {
        return value == null ? null : new BookingId(value);
    }
}
