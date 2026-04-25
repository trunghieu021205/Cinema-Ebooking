package com.cinemaebooking.backend.common.validation.factory;

import com.cinemaebooking.backend.common.validation.domain.*;

/**
 * ValidationFactory - Entry point to domain validation profiles.
 * Responsibility:
 * - Centralize access to validation profiles
 * - Decouple validators from direct profile dependencies
 * - Provide scalable entry point for multi-domain validation
 * Note:
 * - Lightweight abstraction (no Spring bean)
 * @author Hieu Nguyen
 * @since 2026
 */
public final class ValidationFactory {

    private ValidationFactory() {}

    public static CinemaValidationProfile cinema() {
        return CinemaValidationProfile.INSTANCE;
    }
    public static RoomValidationProfile   room(){ return RoomValidationProfile.INSTANCE;}
    public static SeatValidationProfile seat(){ return SeatValidationProfile.INSTANCE;}
    public static SeatTypeValidationProfile seatType(){ return SeatTypeValidationProfile.INSTANCE;}
    public static UserValidationProfile user(){ return UserValidationProfile.INSTANCE;}
    public static ShowtimeValidationProfile showTime() { return ShowtimeValidationProfile.INSTANCE;}
}
