package com.cinemaebooking.backend.user.application.dto.Response;

import com.cinemaebooking.backend.user.domain.enums.UserRole;
import com.cinemaebooking.backend.user.domain.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String avatarUrl;
    private UserRole role;
    private UserStatus status;
}
