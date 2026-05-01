package com.cinemaebooking.backend.common.exception.domain;

import com.cinemaebooking.backend.common.exception.BaseException;
import com.cinemaebooking.backend.common.exception.ErrorCode;
import com.cinemaebooking.backend.showtime.domain.valueobject.ShowtimeFormatId;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * ShowtimeFormatExceptions - Domain-specific exceptions for ShowtimeFormat.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ShowtimeFormatExceptions {

    // ================== NOT FOUND ==================

    public static BaseException notFound(ShowtimeFormatId id) {
        return new BaseException(
                ErrorCode.SHOWTIME_FORMAT_NOT_FOUND,
                "Showtime format not found: " + id
        );
    }

    // ================== BUSINESS RULE ==================

    public static BaseException duplicateName(String name) {
        return new BaseException(
                ErrorCode.SHOWTIME_FORMAT_INVALID,
                "Showtime format name already exists: " + name
        );
    }

    public static BaseException invalidName(String name) {
        return new BaseException(
                ErrorCode.INVALID_INPUT,
                "Invalid format name: " + name + " (must not be null or blank)"
        );
    }

    public static BaseException invalidPrice(Long price) {
        return new BaseException(
                ErrorCode.INVALID_INPUT,
                "Invalid extra price: " + price + " (must be >= 0)"
        );
    }
}