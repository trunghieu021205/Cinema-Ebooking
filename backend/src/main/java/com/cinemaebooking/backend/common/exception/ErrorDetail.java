package com.cinemaebooking.backend.common.exception;

public record ErrorDetail(
        String field,           // "email", "seatId", "couponCode"
        ErrorCategory category, // DUPLICATE, INVALID_FORMAT...
        String reason           // "must be a valid email address" — controlled by rule, không phải dev tự viết
) {}
