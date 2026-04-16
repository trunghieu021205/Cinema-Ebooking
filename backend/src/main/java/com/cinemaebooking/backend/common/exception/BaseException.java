package com.cinemaebooking.backend.common.exception;

import lombok.Getter;

/**
 * BaseException - Root exception cho toàn bộ hệ thống (Clean Architecture + DDD).
 *
 * RULES:
 * - ALL exceptions in the system MUST extend this class.
 * - ALWAYS use ErrorCode, never throw raw RuntimeException or plain string message.
 * - Custom message is allowed only when additional business context is needed.
 *
 * @author Hieu Nguyen
 * @since 2026
 */
@Getter
public class BaseException extends RuntimeException {

    private final ErrorCode errorCode;

    public BaseException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public BaseException(ErrorCode errorCode, String customMessage) {
        super(customMessage != null ? customMessage : errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public BaseException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
    }

    public BaseException(ErrorCode errorCode, String customMessage, Throwable cause) {
        super(customMessage != null ? customMessage : errorCode.getMessage(), cause);
        this.errorCode = errorCode;
    }

    /**
     * Helper cho GlobalExceptionHandler
     */
    public int getHttpStatus() {
        return errorCode.getHttpStatus();
    }
}