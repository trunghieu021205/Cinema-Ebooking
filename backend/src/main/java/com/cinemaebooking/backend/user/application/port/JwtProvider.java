package com.cinemaebooking.backend.user.application.port;

import com.cinemaebooking.backend.user.domain.valueObject.UserId;

/**
 * Outbound port: JWT generation and validation.
 * Implemented in infrastructure using a JWT library (e.g. jjwt).
 */
public interface JwtProvider {
    String generateToken(UserId userId, String role);
    String generateResetToken(UserId userId);
    UserId extractUserId(String token);
}
