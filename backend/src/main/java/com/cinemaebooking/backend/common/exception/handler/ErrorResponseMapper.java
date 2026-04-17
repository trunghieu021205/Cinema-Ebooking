package com.cinemaebooking.backend.common.exception.handler;

import com.cinemaebooking.backend.common.exception.BaseException;
import com.cinemaebooking.backend.common.exception.ErrorCode;
import com.cinemaebooking.backend.common.exception.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
/**
 * @author Hieu Nguyen
 * @since 2026
*/
public final class ErrorResponseMapper {

    private ErrorResponseMapper() {}

    public static ErrorResponse fromBaseException(BaseException ex, HttpServletRequest request) {

        return ErrorResponse.builder()
                .code(ex.getErrorCode().getCode())
                .message(ex.getMessage())
                .status(resolveHttpStatus(ex.getErrorCode()))
                .timestamp(Instant.now())
                .path(request.getRequestURI())
                .traceId(UUID.randomUUID().toString())
                .build();
    }

    public static ErrorResponse fromValidation(List<String> details, HttpServletRequest request) {

        return ErrorResponse.builder()
                .code(ErrorCode.INVALID_INPUT.getCode())
                .message("Validation failed")
                .status(ErrorCode.INVALID_INPUT.getHttpStatus())
                .timestamp(Instant.now())
                .path(request.getRequestURI())
                .traceId(UUID.randomUUID().toString())
                .details(details)
                .build();
    }

    public static ErrorResponse fromGeneric(Exception ex, HttpServletRequest request) {

        return ErrorResponse.builder()
                .code(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode())
                .message(ex.getMessage() != null ? ex.getMessage() : "Unexpected error")
                .status(ErrorCode.UNCATEGORIZED_EXCEPTION.getHttpStatus())
                .timestamp(Instant.now())
                .path(request.getRequestURI())
                .traceId(UUID.randomUUID().toString())
                .build();
    }

    private static int resolveHttpStatus(ErrorCode code) {
        return code.getHttpStatus();
    }
}