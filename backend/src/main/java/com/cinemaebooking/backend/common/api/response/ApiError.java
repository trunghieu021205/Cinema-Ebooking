package com.cinemaebooking.backend.common.api.response;

public record ApiError(
        String code,
        String message,
        Object details
) {}