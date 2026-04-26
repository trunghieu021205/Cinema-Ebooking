package com.cinemaebooking.backend.user.application.dto.Response;

import com.cinemaebooking.backend.user.domain.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {
    private String accessToken;
    private UserRole role;
}
