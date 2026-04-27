package com.cinemaebooking.backend.common.exception;

import lombok.Getter;

import java.util.List;

/**
 * BaseException - Root exception for the entire system.
 * Responsibility:
 * - Hold error code, structured details, and optional debug message
 * - super.getMessage() ALWAYS returns errorCode.getMessage() — never dev-written strings
 * - debugMessage là thông tin kỹ thuật nội bộ, chỉ xuất hiện trong log, không bao giờ trả về FE
 *
 * @author Hieu Nguyen
 * @since 2026
 */
@Getter
public class BaseException extends RuntimeException {

    private final ErrorCode errorCode;
    private final List<ErrorDetail> details;

    /**
     * Thông tin debug kỹ thuật — KHÔNG bao giờ trả về FE.
     * Ví dụ: "Redis timeout on seat A5 after 3 retries"
     * Xuất hiện duy nhất trong log của GlobalExceptionHandler.
     */
    private final String debugMessage;

    // ===== Constructor 1: không có thêm thông tin =====
    public BaseException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.details = List.of();
        this.debugMessage = null;
    }

    // ===== Constructor 2: dev truyền message → tự động demote xuống debugMessage =====
    public BaseException(ErrorCode errorCode, String debugMessage) {
        super(errorCode.getMessage()); // ← override: FE luôn nhận message từ ErrorCode
        this.errorCode = errorCode;
        this.details = List.of();
        this.debugMessage = debugMessage;
    }

    // ===== Constructor 3: có cause =====
    public BaseException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
        this.details = List.of();
        this.debugMessage = null;
    }

    // ===== Constructor 4: có cả debugMessage lẫn cause =====
    public BaseException(ErrorCode errorCode, String debugMessage, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
        this.details = List.of();
        this.debugMessage = debugMessage;
    }

    // ===== Constructor 5 (MỚI): structured validation errors =====
    public BaseException(ErrorCode errorCode, List<ErrorDetail> details) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.details = details != null ? details : List.of();
        this.debugMessage = null;
    }
}