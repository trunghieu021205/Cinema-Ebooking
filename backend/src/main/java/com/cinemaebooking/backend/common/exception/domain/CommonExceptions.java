package com.cinemaebooking.backend.common.exception.domain;

import com.cinemaebooking.backend.common.exception.BaseException;
import com.cinemaebooking.backend.common.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * CommonExceptions - System-wide generic exceptions.
 * Responsibility:
 * - Handle non-domain-specific errors
 * - Validation, security, and infrastructure-related exceptions
 * @author Hieu Nguyen
 * @since 2026
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CommonExceptions {

    // ================== RESOURCE ==================

    public static BaseException resourceNotFound() {
        return new BaseException(ErrorCode.RESOURCE_NOT_FOUND);
    }

    public static BaseException resourceNotFound(String message) {
        return new BaseException(ErrorCode.RESOURCE_NOT_FOUND, message);
    }

    public static BaseException resourceAlreadyExists(String message) {
        return new BaseException(ErrorCode.RESOURCE_ALREADY_EXISTS, message);
    }

    // ================== VALIDATION ==================

    public static BaseException invalidInput(String message) {
        return new BaseException(ErrorCode.INVALID_INPUT, message);
    }

    // ================== SECURITY ==================

    public static BaseException unauthorized() {
        return new BaseException(ErrorCode.UNAUTHORIZED);
    }

    public static BaseException forbidden() {
        return new BaseException(ErrorCode.FORBIDDEN);
    }

    // ================== SYSTEM ==================

    public static BaseException concurrencyConflict() {
        return new BaseException(ErrorCode.CONCURRENCY_CONFLICT);
    }
}