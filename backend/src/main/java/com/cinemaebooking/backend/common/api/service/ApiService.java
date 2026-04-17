package com.cinemaebooking.backend.common.api.service;

import com.cinemaebooking.backend.common.api.response.ApiResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.time.Instant;

/**
 * ApiService - Helper service to manually build ApiResponse when needed.
 * Note: Most cases should rely on ApiResponseAdvice for automatic wrapping.
 * Use this service only for special cases where manual wrapping is required.
 */
@Service
public class ApiService {

    public <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .data(data)
                .timestamp(Instant.now())
                .traceId(getCurrentTraceId())
                .build();
    }

    private String getCurrentTraceId() {
        String traceId = MDC.get("traceId");
        return traceId != null ? traceId : "no-trace-id";
    }
}