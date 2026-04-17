package com.cinemaebooking.backend.common.exception.domain;

import com.cinemaebooking.backend.cinema.domain.valueobject.CinemaId;
import com.cinemaebooking.backend.common.exception.BaseException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * CinemaExceptions - Domain-specific exceptions for Cinema.
 * Responsibility:
 * - Provide semantic exception methods for cinema domain
 * - Delegate to CommonExceptions for consistent error handling
 * - Improve readability in use cases & validators
 *
 * @author Hieu Nguyen
 * @since 2026
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CinemaExceptions {

    // ================== NOT FOUND ==================

    public static BaseException notFound(CinemaId id) {
        return CommonExceptions.resourceNotFound(
                "Cinema not found with id: " + id
        );
    }

    // ================== DUPLICATE ==================

    public static BaseException duplicateCinemaName(String name) {
        return CommonExceptions.resourceAlreadyExists(
                "Cinema name already exists: " + name
        );
    }

    public static BaseException duplicateCinemaLocation(String address, String city) {
        return CommonExceptions.resourceAlreadyExists(
                "Cinema already exists at address: " + address + ", city: " + city
        );
    }

    // ================== BUSINESS RULE ==================

    public static BaseException inactiveCinema(CinemaId id) {
        return CommonExceptions.invalidInput(
                "Cinema is inactive: " + id
        );
    }
}