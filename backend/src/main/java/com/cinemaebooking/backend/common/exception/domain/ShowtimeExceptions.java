package com.cinemaebooking.backend.common.exception.domain;

import com.cinemaebooking.backend.common.exception.BaseException;
import com.cinemaebooking.backend.common.exception.ErrorCode;
import com.cinemaebooking.backend.showtime.domain.valueobject.ShowtimeId;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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

    // ================== BUSINESS RULE ==================

    public static BaseException invalidTimeRange(
            LocalDateTime startTime,
            LocalDateTime endTime
    ) {
        return new BaseException(
                ErrorCode.INVALID_INPUT,
                "Invalid showtime time range: startTime=" + startTime +
                        ", endTime=" + endTime +
                        " (startTime must be before endTime)"
        );
    }

    public static BaseException roomTimeConflict(
            Long roomId,
            LocalDateTime startTime,
            LocalDateTime endTime
    ) {
        return new BaseException(
                ErrorCode.SHOWTIME_ROOM_CONFLICT,
                "Showtime conflict in room=" + roomId +
                        ", startTime=" + startTime +
                        ", endTime=" + endTime
        );
    }
}