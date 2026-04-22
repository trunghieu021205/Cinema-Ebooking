package com.cinemaebooking.backend.seat.domain.enums;

public enum SeatStatus {
    ACTIVE,
    INACTIVE;

    public boolean isActive() {
        return this == ACTIVE;
    }
}
