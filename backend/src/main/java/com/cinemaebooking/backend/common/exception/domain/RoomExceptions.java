package com.cinemaebooking.backend.common.exception.domain;

import com.cinemaebooking.backend.common.exception.BaseException;
import com.cinemaebooking.backend.common.exception.ErrorCode;
import com.cinemaebooking.backend.room.domain.valueObject.RoomId;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

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

    // ================== DUPLICATE ==================

    public static BaseException duplicateRoomName(String name) {
        return new BaseException(ErrorCode.ROOM_ALREADY_EXISTS,
                "Room already exist with: " + name);
    }

    public static BaseException duplicateRoomInCinema(String name, Long cinemaId) {
        return new BaseException(ErrorCode.ROOM_ALREADY_EXISTS,
                "Room already exists with name: " + name + " in cinema: " + cinemaId
        );
    }

    // ================== BUSINESS RULE ==================

    public static BaseException inactiveRoom(RoomId id) {
        return CommonExceptions.invalidInput(
                "Room is inactive: " + id
        );
    }

    public static BaseException invalidCapacity(Integer seats) {
        String message = "Room capacity must not be null";
        if(seats != null) {
            message = "Room capacity must be positive";
        }
        return CommonExceptions.invalidInput(
                message
        );
    }
}
