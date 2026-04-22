package com.cinemaebooking.backend.common.exception.domain;

import com.cinemaebooking.backend.cinema.domain.valueobject.CinemaId;
import com.cinemaebooking.backend.common.exception.BaseException;
import com.cinemaebooking.backend.common.exception.ErrorCode;
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
        return new BaseException(ErrorCode.CINEMA_NOT_FOUND,
                "Cinema not found with id: " + id
        );
    }

    // ================== DUPLICATE ==================

    public static BaseException duplicateCinemaName(String name) {
        return new BaseException(ErrorCode.CINEMA_ALREADY_EXISTS,
                "Cinema name already exists: " + name
        );
    }

    public static BaseException duplicateCinemaLocation(String address, String city) {
        return new BaseException(ErrorCode.CINEMA_ALREADY_EXISTS,
                "Cinema already exists at address: " + address + ", city: " + city
        );
    }
}