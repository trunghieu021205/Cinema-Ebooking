package com.cinemaebooking.backend.common.exception;

import lombok.Getter;

/**
 * ErrorCode - Centralized error definition for entire system.
 * RULES:
 * 1. DO NOT create new error enums inside domain packages.
 * 2. ONLY ADD new error codes here by backend lead/maintainer.
 * 3. DO NOT modify existing error code numbers once released.
 * 4. Frontend relies on "code" field for logic handling.
 * 5. message is default fallback, can be overridden in exception if needed.
 * ARCHITECTURE RULE:
 * - All exceptions MUST use ErrorCode
 * - No raw RuntimeException or string message throwing allowed
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

    // ===================== COMMON =====================
    RESOURCE_NOT_FOUND(3001, "Resource not found", 404),
    RESOURCE_ALREADY_EXISTS(3002, "Resource already exists", 409);;

    private final int code;
    private final String message;
    private final int httpStatus;

    ErrorCode(int code, String message, int httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}