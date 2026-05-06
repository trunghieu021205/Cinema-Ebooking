package com.cinemaebooking.backend.common.api.response;

import com.cinemaebooking.backend.common.exception.ErrorType;

import java.util.Map;

public record ApiError(
        int code,
        String message,
        ErrorType type,
        Map<String,Object> params,
        Object details
) {}