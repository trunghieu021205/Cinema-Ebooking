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

import java.util.List;
import java.util.UUID;

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

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(
            BaseException ex, HttpServletRequest request) {

        String traceId = UUID.randomUUID().toString();

        ErrorResponse response =
                ErrorResponseMapper.fromBaseException(ex, request);

        log.warn("Business exception [{}] - {}",
                traceId, ex.getMessage());

        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        String traceId = UUID.randomUUID().toString();

        List<String> details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .toList();

        ErrorResponse response =
                ErrorResponseMapper.fromValidation(details, request);

        log.warn("Validation failed [{}]: {}", traceId, details);

        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex, HttpServletRequest request) {

        String traceId = UUID.randomUUID().toString();

        log.error("Unexpected exception [{}]", traceId, ex);

        ErrorResponse response =
                ErrorResponseMapper.fromGeneric(ex, request);

        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }
}