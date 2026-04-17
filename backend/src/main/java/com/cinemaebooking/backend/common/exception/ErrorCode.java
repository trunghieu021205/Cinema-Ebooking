package com.cinemaebooking.backend.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
/**
 * ErrorCode - Centralized error definition for entire system.
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
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_INPUT(1001, "Invalid input", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED(1002, "Unauthorized", HttpStatus.UNAUTHORIZED),
    FORBIDDEN(1003, "Forbidden", HttpStatus.FORBIDDEN),
    CONCURRENCY_CONFLICT(1004, "Concurrency conflict, please try again", HttpStatus.CONFLICT),
    INVALID_REQUEST(1005, "Invalid request", HttpStatus.BAD_REQUEST),
    CONFLICT(1006, "Conflict", HttpStatus.CONFLICT),

    // ===================== CINEMA =====================
    CINEMA_NOT_FOUND(2001, "Cinema not found", HttpStatus.NOT_FOUND),
    CINEMA_ALREADY_EXISTS(2002, "Cinema with this name already exists", HttpStatus.CONFLICT),
    CINEMA_INVALID_STATUS(2003, "Invalid cinema status", HttpStatus.BAD_REQUEST),

    // ===================== ROOM =====================
    ROOM_NOT_FOUND(2101, "Room not found", HttpStatus.NOT_FOUND),
    ROOM_ALREADY_EXISTS(2102, "Room already exists in this cinema", HttpStatus.CONFLICT),
    ROOM_INVALID_STATUS(2103, "Invalid room status", HttpStatus.BAD_REQUEST),
    ROOM_INVALID_CAPACITY(2104, "Invalid room capacity", HttpStatus.BAD_REQUEST),

    // ===================== COMMON =====================
    RESOURCE_NOT_FOUND(3001, "Resource not found", HttpStatus.NOT_FOUND),
    RESOURCE_ALREADY_EXISTS(3002, "Resource already exists", HttpStatus.CONFLICT);

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;

    ErrorCode(int code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}