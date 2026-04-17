package com.cinemaebooking.backend.common.exception;

import lombok.Getter;

/**
 * BaseException - Root exception for the entire system.
 * Responsibility:
 * - Hold error code and message
 * - Provide unified exception structure
 * Note:
 * - NO HTTP concerns inside this class
 * - HTTP mapping should be handled in GlobalExceptionHandler
 */
@Getter
public class BaseException extends RuntimeException {

    private final ErrorCode errorCode;

    public BaseException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public BaseException(ErrorCode errorCode, String message) {
        super(message != null ? message : errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public BaseException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
    }

    public BaseException(ErrorCode errorCode, String message, Throwable cause) {
        super(message != null ? message : errorCode.getMessage(), cause);
        this.errorCode = errorCode;
    }
}