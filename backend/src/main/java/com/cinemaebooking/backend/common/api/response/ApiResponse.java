package com.cinemaebooking.backend.common.api.response;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

/**
 * ApiResponse - Standard success response wrapper.
 * This class is automatically wrapped by ApiResponseAdvice
 * to ensure consistent API response format.
 */
@Getter
@Builder
public class ApiResponse<T> {

    private final T data;
    private final Instant timestamp;
    private final String traceId;
}