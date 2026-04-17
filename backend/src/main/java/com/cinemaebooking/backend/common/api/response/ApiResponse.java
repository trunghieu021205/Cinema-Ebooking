package com.cinemaebooking.backend.common.api.response;

import java.time.Instant;

/**
 * Unified API response wrapper (success + error)
 */
public record ApiResponse<T>(
        T data,
        ApiError error,
        Instant timestamp,
        String traceId
) {
    public ApiResponse {
        if (timestamp == null) {
            timestamp = Instant.now();
        }
    }

    public static <T> ApiResponse<T> success(T data, String traceId) {
        return new ApiResponse<>(data, null, Instant.now(), traceId);
    }

    public static <T> ApiResponse<T> error(ApiError error, String traceId) {
        return new ApiResponse<>(null, error, Instant.now(), traceId);
    }
}