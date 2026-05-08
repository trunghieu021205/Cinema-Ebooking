package com.cinemaebooking.backend.user.application.dto.Response;

public record LoginResponse(String accessToken, String refreshToken, String role) { }
