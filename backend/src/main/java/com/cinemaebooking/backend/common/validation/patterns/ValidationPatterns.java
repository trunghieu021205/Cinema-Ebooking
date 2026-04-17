package com.cinemaebooking.backend.common.validation.patterns;

/**
 * ValidationPatterns - Centralized regex definitions.
 * Responsibility:
 * - Store reusable regex patterns
 * - Avoid duplication across validation profiles
 * - Ensure consistency of format validation
 */
public final class ValidationPatterns {

    // ===================== CINEMA =====================
    public static final String CITY = "^[\\p{L}\\s\\-\\.]+$";
    public static final String ADDRESS = "^[\\p{L}0-9\\s,\\-./#]+$";
    public static final String CINEMA_NAME = "^[\\p{L}0-9\\s\\-\\(\\)\\.&']+$";

    private ValidationPatterns() {}
}
