package com.cinemaebooking.backend.room_layout.domain.enums;

public enum SeatStatus {
    ACTIVE,
    INACTIVE;

    public boolean isActive() {
        return this == ACTIVE;
    }
}
