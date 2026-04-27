package com.cinemaebooking.backend.common.exception.handler;

import com.cinemaebooking.backend.common.api.response.ApiError;
import com.cinemaebooking.backend.common.api.response.ApiResponse;
import com.cinemaebooking.backend.common.exception.BaseException;
import com.cinemaebooking.backend.common.exception.ErrorCode;
import com.cinemaebooking.backend.common.exception.ErrorDetail;
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

        // message luôn lấy từ ErrorCode — debugMessage chỉ dùng để log
        logByType(traceId, errorCode, request, ex);

        List<ErrorDetail> details = ex.getDetails();

        ApiError error = new ApiError(
                errorCode.getCode(),
                errorCode.getMessage(),             // ← không bao giờ dùng ex.getMessage()
                errorCode.getType(),
                details.isEmpty() ? null : details  // null nếu không có detail
        );

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(buildResponse(error, traceId, request));
    }

    // ================== SPRING VALIDATION (@Valid) ==================

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        String traceId = getTraceId();
        ErrorCode errorCode = ErrorCode.INVALID_INPUT;

        // Map Spring FieldError → ErrorDetail
        List<ErrorDetail> details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> new ErrorDetail(
                        e.getField(),
                        null,                        // Spring không có ErrorCategory
                        e.getDefaultMessage()
                ))
                .toList();

        log.warn("[{}] Validation failed | path={} | details={}",
                traceId,
                request.getRequestURI(),
                details
        );

        ApiError error = new ApiError(
                errorCode.getCode(),
                errorCode.getMessage(),
                errorCode.getType(),
                details
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(buildResponse(error, traceId, request));
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

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildResponse(error, traceId, request));
    }

    // ================== HELPER ==================

    private String getTraceId() {
        String traceId = MDC.get(TRACE_ID_KEY);
        return (traceId != null && !traceId.isBlank()) ? traceId : "UNKNOWN";
    }

    private void logByType(
            String traceId,
            ErrorCode errorCode,
            HttpServletRequest request,
            BaseException ex
    ) {
        String debugInfo = ex.getDebugMessage() != null
                ? " | debug: " + ex.getDebugMessage()
                : "";

        if (errorCode.getType() == ErrorType.TECHNICAL) {
            log.error("[{}] Technical error | code={} | path={}{}",
                    traceId,
                    errorCode.getCode(),
                    request.getRequestURI(),
                    debugInfo,
                    ex
            );
        } else {
            log.warn("[{}] Business error | code={} | path={}{}",
                    traceId,
                    errorCode.getCode(),
                    request.getRequestURI(),
                    debugInfo
            );
        }
    }

    private ApiResponse<?> buildResponse(
            ApiError error,
            String traceId,
            HttpServletRequest request
    ) {
        return ApiResponse.error(error, traceId, request.getRequestURI());
    }
}