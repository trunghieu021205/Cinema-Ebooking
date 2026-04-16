package com.cinemaebooking.backend.common.exception.handler;

import com.cinemaebooking.backend.common.exception.BaseException;
import com.cinemaebooking.backend.common.exception.ErrorCode;
import com.cinemaebooking.backend.common.exception.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * GlobalExceptionHandler - Centralized exception handling for the entire application.
 * All business exceptions must go through XXXExceptions factories.
 *
 * @author Hieu Nguyen
 * @since 2026
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handle all business exceptions created via Exception Factories
     * (CinemaExceptions, CommonExceptions, ...)
     */
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(
            BaseException ex, HttpServletRequest request) {

        String traceId = UUID.randomUUID().toString();

        ErrorResponse response = ErrorResponse.builder()
                .code(ex.getErrorCode().getCode())
                .message(ex.getMessage())
                .status(ex.getHttpStatus())
                .timestamp(Instant.now())
                .path(request.getRequestURI())
                .traceId(traceId)
                .build();

        log.warn("Business exception [{}] - Code: {} - Message: {}",
                traceId, ex.getErrorCode().getCode(), ex.getMessage());

        return ResponseEntity
                .status(ex.getHttpStatus())
                .body(response);
    }

    /**
     * Handle validation errors from @Valid / @Validated
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        String traceId = UUID.randomUUID().toString();

        List<String> details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());

        ErrorResponse response = ErrorResponse.builder()
                .code(ErrorCode.INVALID_INPUT.getCode())
                .message("Validation failed")
                .status(ErrorCode.INVALID_INPUT.getHttpStatus())
                .timestamp(Instant.now())
                .path(request.getRequestURI())
                .traceId(traceId)
                .details(details)
                .build();

        log.warn("Validation failed [{}]: {}", traceId, details);

        return ResponseEntity
                .status(ErrorCode.INVALID_INPUT.getHttpStatus())
                .body(response);
    }

    /**
     * Fallback handler for all unhandled exceptions
     * This should rarely happen if exception architecture is followed properly
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex, HttpServletRequest request) {

        String traceId = UUID.randomUUID().toString();

        log.error("Unexpected exception occurred [{}]", traceId, ex);

        ErrorResponse response = ErrorResponse.builder()
                .code(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode())
                .message(ex.getMessage() != null ? ex.getMessage() : "An unexpected error occurred")
                .status(ErrorCode.UNCATEGORIZED_EXCEPTION.getHttpStatus())
                .timestamp(Instant.now())
                .path(request.getRequestURI())
                .traceId(traceId)
                .build();

        return ResponseEntity
                .status(ErrorCode.UNCATEGORIZED_EXCEPTION.getHttpStatus())
                .body(response);
    }
}