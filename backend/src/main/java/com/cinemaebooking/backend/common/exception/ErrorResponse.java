package com.cinemaebooking.backend.common.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.List;

/**
 * ErrorResponse - Standardized error response DTO for the entire API.
 *
 * @author Hieu Nguyen
 * @since 2026
 */
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private final int code;
    private final String message;
    private final int status;
    private final Instant timestamp;
    private final String path;
    private final String traceId;
    private final List<String> details;   // Dùng cho validation errors hoặc thêm thông tin chi tiết
}