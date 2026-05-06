package com.cinemaebooking.backend.common.exception.domain;

import com.cinemaebooking.backend.cinema.domain.valueobject.CinemaId;
import com.cinemaebooking.backend.common.exception.BaseException;
import com.cinemaebooking.backend.common.exception.ErrorCategory;
import com.cinemaebooking.backend.common.exception.ErrorCode;
import com.cinemaebooking.backend.common.exception.ErrorDetail;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

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

    public static BaseException notFound(CinemaId id) {
        return new BaseException(ErrorCode.CINEMA_NOT_FOUND,
                "Không tìm thấy rạp với id: " + id);           // debugMessage
    }

    public static BaseException hasRoom(String cinemaName){
        return new BaseException(ErrorCode.CINEMA_HAS_UNDELETED_ROOMS, Map.of("cinemaName", cinemaName));
    }
}