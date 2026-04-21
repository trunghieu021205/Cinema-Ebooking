package com.cinemaebooking.backend.seat.domain.enums;

public enum SeatStatus {
    AVAILABLE,
    BOOKED,
    DISABLED;

    public boolean isActive() {
        return this == AVAILABLE;
    }
}
