package com.cinemaebooking.backend.user.application.port;

/**
 * Outbound port: password hashing abstraction.
 * Implemented in infrastructure using BCrypt.
 */
public interface PasswordEncoder {
    String encode(String rawPassword);
    boolean matches(String rawPassword, String encodedPassword);
}
