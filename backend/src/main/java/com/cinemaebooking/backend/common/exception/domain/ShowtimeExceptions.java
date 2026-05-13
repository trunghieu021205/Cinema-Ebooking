package com.cinemaebooking.backend.common.exception.domain;

import com.cinemaebooking.backend.common.exception.BaseException;
import com.cinemaebooking.backend.common.exception.ErrorCode;
import com.cinemaebooking.backend.showtime.domain.valueobject.ShowtimeId;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * ShowtimeExceptions - Domain-specific exceptions for Showtime.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ShowtimeExceptions {

    // ================== NOT FOUND ==================

    public static BaseException notFound(ShowtimeId id) {
        return new BaseException(
                ErrorCode.SHOWTIME_NOT_FOUND,
                "Showtime not found: " + id
        );
    }
}