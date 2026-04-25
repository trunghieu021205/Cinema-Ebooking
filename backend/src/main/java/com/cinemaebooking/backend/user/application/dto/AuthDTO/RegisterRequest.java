package com.cinemaebooking.backend.user.application.dto.AuthDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    private String fullName;
    private String email;
    private String password;
    private String phoneNumber;
}
