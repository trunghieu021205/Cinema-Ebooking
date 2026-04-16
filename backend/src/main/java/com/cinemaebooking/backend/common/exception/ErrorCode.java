package com.cinemaebooking.backend.common.exception;

import lombok.Getter;

/**
 * ErrorCode - Centralized error definition for entire system.
 *
 * STRICT RULES:
 * 1. ONLY Backend Lead/Maintainer is allowed to add or modify error codes.
 * 2. DO NOT create new error enums inside any domain packages.
 * 3. DO NOT modify existing error code numbers once they are released (Frontend depends on it).
 * 4. ALL exceptions MUST be thrown via corresponding Exception Factories:
 *    → CommonExceptions, CinemaExceptions, RoomExceptions, ...
 * 5. NEVER throw raw RuntimeException, new BaseException(...) directly, or use ErrorCode directly in business code.
 * 6. Message in ErrorCode is the default message. It can be overridden when throwing exception if needed.
 *
 * @author Hieu Nguyen
 * @since 2026
 */
@Getter
public enum ErrorCode {

    // ===================== SYSTEM =====================
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", 500),
    INVALID_INPUT(1001, "Invalid input", 400),
    UNAUTHORIZED(1002, "Unauthorized", 401),
    FORBIDDEN(1003, "Forbidden", 403),
    CONCURRENCY_CONFLICT(1004, "Concurrency conflict, please try again", 409),

    // ===================== CINEMA =====================
    CINEMA_NOT_FOUND(2001, "Cinema not found", 404),
    CINEMA_ALREADY_EXISTS(2002, "Cinema with this name already exists", 409),
    CINEMA_INVALID_STATUS(2003, "Invalid cinema status", 400),

    // ===================== ROOM =====================
    ROOM_NOT_FOUND(2101, "Room not found", 404),
    ROOM_ALREADY_EXISTS(2102, "Room already exists in this cinema", 409),
    ROOM_INVALID_STATUS(2103, "Invalid room status", 400),
    ROOM_INVALID_CAPACITY(2104, "Invalid room capacity", 400),

    // ===================== COMMON =====================
    RESOURCE_NOT_FOUND(3001, "Resource not found", 404),
    RESOURCE_ALREADY_EXISTS(3002, "Resource already exists", 409);

    private final int code;
    private final String message;
    private final int httpStatus;

    ErrorCode(int code, String message, int httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}