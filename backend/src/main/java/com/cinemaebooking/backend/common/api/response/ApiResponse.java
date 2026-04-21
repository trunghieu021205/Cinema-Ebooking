package com.cinemaebooking.backend.common.api.response;

import java.time.Instant;

/**
 * Unified API response wrapper (success + error)
 */
public record ApiResponse<T>(
        boolean success,
        T data,
        ApiError error,
        Instant timestamp,
        String traceId,
        String path
) {
    public ApiResponse {
        if (timestamp == null) {
            timestamp = Instant.now();
        }
    }

    // ================== SUCCESS ==================
    public static <T> ApiResponse<T> success(T data, String traceId, String path) {
        return new ApiResponse<>(
                true,
                data,
                null,
                Instant.now(),
                traceId,
                path
        );
    }

    // giữ lại để backward compatible
    public static <T> ApiResponse<T> success(T data, String traceId) {
        return success(data, traceId, null);
    }

    // ================== ERROR ==================
    public static <T> ApiResponse<T> error(ApiError error, String traceId, String path) {
        return new ApiResponse<>(
                false,
                null,
                error,
                Instant.now(),
                traceId,
                path
        );
    }

    // giữ lại để backward compatible
    public static <T> ApiResponse<T> error(ApiError error, String traceId) {
        return error(error, traceId, null);
    }
}