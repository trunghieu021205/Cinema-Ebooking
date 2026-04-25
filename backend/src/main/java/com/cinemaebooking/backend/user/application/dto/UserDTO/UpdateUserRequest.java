package com.cinemaebooking.backend.user.application.dto.UserDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {
    private String fullName;
    private String phoneNumber;
    private String avatarUrl;
}
