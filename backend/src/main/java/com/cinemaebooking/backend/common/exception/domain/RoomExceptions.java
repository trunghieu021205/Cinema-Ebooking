package com.cinemaebooking.backend.common.exception.domain;

import com.cinemaebooking.backend.common.exception.BaseException;
import com.cinemaebooking.backend.common.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * RoomExceptions - Business exceptions for Room domain.
 * All room-related exceptions must be thrown through this class only.
 *
 * @author Hieu Nguyen
 * @since 2026
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RoomExceptions {

    public static BaseException notFound() {
        return new BaseException(ErrorCode.ROOM_NOT_FOUND);
    }

    public static BaseException notFound(String message) {
        return new BaseException(ErrorCode.ROOM_NOT_FOUND, message);
    }

    public static BaseException alreadyExists() {
        return new BaseException(ErrorCode.ROOM_ALREADY_EXISTS);
    }

    public static BaseException alreadyExists(String message) {
        return new BaseException(ErrorCode.ROOM_ALREADY_EXISTS, message);
    }

    public static BaseException invalidStatus() {
        return new BaseException(ErrorCode.ROOM_INVALID_STATUS);
    }

    public static BaseException invalidStatus(String message) {
        return new BaseException(ErrorCode.ROOM_INVALID_STATUS, message);
    }

    public static BaseException invalidCapacity() {
        return new BaseException(ErrorCode.ROOM_INVALID_CAPACITY);
    }

    public static BaseException invalidCapacity(String message) {
        return new BaseException(ErrorCode.ROOM_INVALID_CAPACITY, message);
    }
}