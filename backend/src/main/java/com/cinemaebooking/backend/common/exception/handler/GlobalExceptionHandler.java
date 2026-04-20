package com.cinemaebooking.backend.common.exception.handler;

import com.cinemaebooking.backend.common.api.response.ApiError;
import com.cinemaebooking.backend.common.api.response.ApiResponse;
import com.cinemaebooking.backend.common.exception.BaseException;
import com.cinemaebooking.backend.common.exception.ErrorCode;
import com.cinemaebooking.backend.common.exception.ErrorType;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String TRACE_ID_KEY = "traceId";

    // ================== BUSINESS / DOMAIN EXCEPTION ==================
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiResponse<?>> handleBaseException(
            BaseException ex,
            HttpServletRequest request
    ) {

        String traceId = getTraceId();
        ErrorCode errorCode = ex.getErrorCode();

        String message = resolveMessage(ex, errorCode);

        logByType(traceId, errorCode, request, message, ex);

        ApiError error = new ApiError(
                errorCode.getCode(),
                message,
                errorCode.getType(),
                null
        );

        ApiResponse<?> response = buildResponse(error, traceId, request);

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(response);
    }

    // ================== VALIDATION ERROR ==================
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {

        String traceId = getTraceId();

        List<String> details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .toList();

        ErrorCode errorCode = ErrorCode.INVALID_INPUT;

        log.warn("[{}] Validation failed | path={} | details={}",
                traceId,
                request.getRequestURI(),
                details
        );

        ApiError error = new ApiError(
                errorCode.getCode(),
                "Request validation failed",
                errorCode.getType(),
                details
        );

        ApiResponse<?> response = buildResponse(error, traceId, request);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    // ================== GENERIC / UNEXPECTED ERROR ==================
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGenericException(
            Exception ex,
            HttpServletRequest request
    ) {

        String traceId = getTraceId();
        ErrorCode errorCode = ErrorCode.UNCATEGORIZED_EXCEPTION;

        log.error("[{}] Unexpected error | path={}",
                traceId,
                request.getRequestURI(),
                ex
        );

        ApiError error = new ApiError(
                errorCode.getCode(),
                errorCode.getMessage(),
                errorCode.getType(),
                null
        );

        ApiResponse<?> response = buildResponse(error, traceId, request);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }

    // ================== HELPER METHODS ==================

    /**
     * Always get traceId from MDC (set by TraceIdFilter)
     */
    private String getTraceId() {
        String traceId = MDC.get(TRACE_ID_KEY);
        return (traceId != null && !traceId.isBlank())
                ? traceId
                : "UNKNOWN"; // fallback (should not happen if filter works correctly)
    }

    private String resolveMessage(BaseException ex, ErrorCode errorCode) {
        return (ex.getMessage() != null && !ex.getMessage().isBlank())
                ? ex.getMessage()
                : errorCode.getMessage();
    }

    private void logByType(
            String traceId,
            ErrorCode errorCode,
            HttpServletRequest request,
            String message,
            Exception ex
    ) {
        if (errorCode.getType() == ErrorType.TECHNICAL) {
            log.error("[{}] Technical error | code={} | path={} | message={}",
                    traceId,
                    errorCode.getCode(),
                    request.getRequestURI(),
                    message,
                    ex
            );
        } else {
            log.warn("[{}] Business error | code={} | path={} | message={}",
                    traceId,
                    errorCode.getCode(),
                    request.getRequestURI(),
                    message
            );
        }
    }

    private ApiResponse<?> buildResponse(
            ApiError error,
            String traceId,
            HttpServletRequest request
    ) {
        return ApiResponse.error(
                error,
                traceId,
                request.getRequestURI()
        );
    }
}