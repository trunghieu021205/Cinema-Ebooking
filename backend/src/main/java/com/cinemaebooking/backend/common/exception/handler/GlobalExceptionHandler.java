package com.cinemaebooking.backend.common.exception.handler;

import com.cinemaebooking.backend.common.api.response.ApiError;
import com.cinemaebooking.backend.common.api.response.ApiResponse;
import com.cinemaebooking.backend.common.exception.BaseException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ================== BUSINESS EXCEPTION ==================
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiResponse<?>> handleBaseException(
            BaseException ex,
            HttpServletRequest request
    ) {

        String traceId = generateTraceId();

        log.warn("[{}] Business error | code={} | path={} | message={}",
                traceId,
                ex.getErrorCode().getCode(),
                request.getRequestURI(),
                ex.getMessage()
        );

        ApiError error = new ApiError(
                String.valueOf(ex.getErrorCode().getCode()),
                ex.getMessage(),
                null
        );

        ApiResponse<?> response = ApiResponse.error(error, traceId);

        return ResponseEntity
                .status(ex.getErrorCode().getHttpStatus())
                .body(response);
    }

    // ================== VALIDATION ERROR ==================
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {

        String traceId = generateTraceId();

        List<String> details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .toList();

        log.warn("[{}] Validation failed | path={} | details={}",
                traceId,
                request.getRequestURI(),
                details
        );

        ApiError error = new ApiError(
                "VALIDATION_ERROR",
                "Request validation failed",
                details
        );

        ApiResponse<?> response = ApiResponse.error(error, traceId);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    // ================== GENERIC ERROR ==================
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGenericException(
            Exception ex,
            HttpServletRequest request
    ) {

        String traceId = generateTraceId();

        log.error("[{}] Unexpected error | path={} ",
                traceId,
                request.getRequestURI(),
                ex
        );

        ApiError error = new ApiError(
                "INTERNAL_SERVER_ERROR",
                "Unexpected error occurred",
                null
        );

        ApiResponse<?> response = ApiResponse.error(error, traceId);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }

    private String generateTraceId() {
        return UUID.randomUUID().toString();
    }
}