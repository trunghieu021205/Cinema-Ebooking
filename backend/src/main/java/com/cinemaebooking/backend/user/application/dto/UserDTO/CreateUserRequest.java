package com.cinemaebooking.backend.user.application.dto.UserDTO;

import com.cinemaebooking.backend.user.domain.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {
    private String fullName;
    private String email;
    private String password;
    private String phoneNumber;
    private String avatarUrl;
    private UserRole role;
}
