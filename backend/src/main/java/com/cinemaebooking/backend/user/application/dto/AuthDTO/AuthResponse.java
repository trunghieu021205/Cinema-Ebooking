package com.cinemaebooking.backend.user.application.dto.AuthDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    private Long userId;
    private String fullName;
    private String email;
    private String role;
    private String accessToken;
    private String refreshToken;
}
