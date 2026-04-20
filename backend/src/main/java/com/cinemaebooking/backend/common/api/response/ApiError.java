package com.cinemaebooking.backend.common.api.response;

import com.cinemaebooking.backend.common.exception.ErrorType;

public record ApiError(
        int code,
        String message,
        ErrorType type,
        Object details
) {}