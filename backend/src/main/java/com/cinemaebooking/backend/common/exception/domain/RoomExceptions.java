package com.cinemaebooking.backend.common.exception.domain;

import com.cinemaebooking.backend.common.exception.BaseException;
import com.cinemaebooking.backend.common.exception.ErrorCode;
import com.cinemaebooking.backend.room.domain.valueObject.RoomId;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * RoomExceptions - Domain-specific exceptions for Room.
 * Responsibility:
 * - Provide semantic exception methods for room domain
 * - Delegate to CommonExceptions for consistent error handling
 * - Improve readability in use cases & validators
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RoomExceptions {

    // ================== NOT FOUND ==================

    public static BaseException notFound(RoomId id) {
        return new BaseException(ErrorCode.ROOM_NOT_FOUND,
                "Room not found: " + id);
    }

    // ================== BUSINESS RULE ==================

    public static BaseException inactiveRoom(RoomId id) {
        return CommonExceptions.invalidInput(
                "Room is inactive: " + id
        );
    }

    public static BaseException deleteBlockedByShowtime(String name){
        return new BaseException(ErrorCode.ROOM_DELETE_BLOCKED_BY_SHOWTIME, Map.of("roomName",name));
    }
}
