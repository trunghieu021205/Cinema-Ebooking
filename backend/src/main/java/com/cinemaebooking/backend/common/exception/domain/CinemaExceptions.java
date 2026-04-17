package com.cinemaebooking.backend.common.exception.domain;

import com.cinemaebooking.backend.common.exception.BaseException;
import com.cinemaebooking.backend.common.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * CinemaExceptions - Business exceptions for Cinema domain.
 * Responsibility:
 * - Handle cinema-specific business rule violations only
 * - Provide consistent exception factory methods
 * Note:
 * - Do NOT include generic/system exceptions here
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CinemaExceptions {

    // ================== NOT FOUND ==================

    public static BaseException notFound() {
        return new BaseException(ErrorCode.CINEMA_NOT_FOUND);
    }

    public static BaseException notFound(String message) {
        return new BaseException(ErrorCode.CINEMA_NOT_FOUND, message);
    }

    // ================== DUPLICATION ==================

    public static BaseException alreadyExists() {
        return new BaseException(ErrorCode.CINEMA_ALREADY_EXISTS);
    }

    public static BaseException alreadyExists(String message) {
        return new BaseException(ErrorCode.CINEMA_ALREADY_EXISTS, message);
    }

    // ================== BUSINESS RULE ==================

    public static BaseException invalidStatus() {
        return new BaseException(ErrorCode.CINEMA_INVALID_STATUS);
    }

    public static BaseException invalidStatus(String message) {
        return new BaseException(ErrorCode.CINEMA_INVALID_STATUS, message);
    }
}