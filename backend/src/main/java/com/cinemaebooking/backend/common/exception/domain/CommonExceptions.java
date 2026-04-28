package com.cinemaebooking.backend.common.exception.domain;

import com.cinemaebooking.backend.common.exception.BaseException;
import com.cinemaebooking.backend.common.exception.ErrorCategory;
import com.cinemaebooking.backend.common.exception.ErrorCode;
import com.cinemaebooking.backend.common.exception.ErrorDetail;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * CommonExceptions - System-wide generic exceptions.
 *
 * @author Hieu Nguyen
 * @since 2026
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CommonExceptions {

    // ================== RESOURCE ==================

    public static BaseException resourceNotFound() {
        return new BaseException(ErrorCode.RESOURCE_NOT_FOUND);
    }

    public static BaseException resourceNotFound(String debugMessage) {
        return new BaseException(ErrorCode.RESOURCE_NOT_FOUND, debugMessage);
    }

    public static BaseException resourceAlreadyExists(String debugMessage) {
        return new BaseException(ErrorCode.RESOURCE_ALREADY_EXISTS, debugMessage);
    }

    // ================== VALIDATION — unstructured (backward-compatible) ==================

    /**
     * @deprecated Dùng invalidInput(field, category, reason) để FE parse được chính xác hơn.
     */
    @Deprecated
    public static BaseException invalidInput(String debugMessage) {
        return new BaseException(ErrorCode.INVALID_INPUT, debugMessage);
    }

    // ================== VALIDATION — structured (preferred) ==================

    /**
     * Single field validation error — FE đọc được field + category + reason.
     */
    public static BaseException invalidInput(
            String field,
            ErrorCategory category,
            String reason
    ) {
        return new BaseException(
                ErrorCode.INVALID_INPUT,
                List.of(new ErrorDetail(field, category, reason))
        );
    }

    /**
     * Multi-field validation error — dùng khi validate batch nhiều field cùng lúc.
     */
    public static BaseException invalidInput(List<ErrorDetail> details) {
        return new BaseException(ErrorCode.INVALID_INPUT, details);
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