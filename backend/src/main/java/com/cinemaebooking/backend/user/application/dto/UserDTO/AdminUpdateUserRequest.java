package com.cinemaebooking.backend.user.application.dto.UserDTO;

import com.cinemaebooking.backend.user.domain.enums.UserRole;
import com.cinemaebooking.backend.user.domain.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AdminUpdateUserRequest {
    private String fullName;
    private String phoneNumber;
    private String avatarUrl;
    private UserRole role;        // ADMIN, STAFF, CUSTOMER
    private UserStatus status;    // ACTIVE, INACTIVE, BANNED...
}
