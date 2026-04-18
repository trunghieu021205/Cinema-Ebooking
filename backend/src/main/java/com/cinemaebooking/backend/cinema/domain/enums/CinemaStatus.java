package com.cinemaebooking.backend.cinema.domain.enums;

/**
 * @author Hieu Nguyen
 * @since 2026
 */
public enum CinemaStatus {
    ACTIVE,
    INACTIVE;

    public boolean isActive() {
        return this == ACTIVE;
    }
}
