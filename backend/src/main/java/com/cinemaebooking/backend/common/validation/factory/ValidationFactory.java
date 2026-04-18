package com.cinemaebooking.backend.common.validation.factory;

import com.cinemaebooking.backend.common.validation.domain.CinemaValidationProfile;

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
}
