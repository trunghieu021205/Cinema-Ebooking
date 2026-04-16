package com.cinemaebooking.backend.common.exception.domain;

import com.cinemaebooking.backend.common.exception.BaseException;
import com.cinemaebooking.backend.common.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * CinemaExceptions - Business exceptions for Cinema domain.
 * All cinema-related exceptions must be thrown through this class only.
 *
 * @author Hieu Nguyen
 * @since 2026
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CinemaExceptions {

    public static BaseException notFound() {
        return new BaseException(ErrorCode.CINEMA_NOT_FOUND);
    }

    public static BaseException notFound(String message) {
        return new BaseException(ErrorCode.CINEMA_NOT_FOUND, message);
    }

    public static BaseException alreadyExists() {
        return new BaseException(ErrorCode.CINEMA_ALREADY_EXISTS);
    }

    public static BaseException alreadyExists(String message) {
        return new BaseException(ErrorCode.CINEMA_ALREADY_EXISTS, message);
    }

    public static BaseException invalidStatus() {
        return new BaseException(ErrorCode.CINEMA_INVALID_STATUS);
    }

    public static BaseException invalidStatus(String message) {
        return new BaseException(ErrorCode.CINEMA_INVALID_STATUS, message);
    }
}