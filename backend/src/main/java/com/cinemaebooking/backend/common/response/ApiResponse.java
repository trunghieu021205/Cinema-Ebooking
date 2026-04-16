package com.cinemaebooking.backend.common.response;

public record ApiResponse<T>(
        int code,
        String message,
        T data
) {
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(2000, "Success", data);
    }
}
