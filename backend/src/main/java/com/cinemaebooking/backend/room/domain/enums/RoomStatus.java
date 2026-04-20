package com.cinemaebooking.backend.room.domain.enums;

public enum RoomStatus {
    ACTIVE,
    INACTIVE,
    MAINTENANCE;

    public boolean isActive() {
        return this == ACTIVE;
    }
}
