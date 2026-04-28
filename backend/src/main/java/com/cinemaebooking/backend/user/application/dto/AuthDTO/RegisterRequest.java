package com.cinemaebooking.backend.user.application.dto.AuthDTO;

import com.cinemaebooking.backend.user.domain.valueObject.UserGender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    private String fullName;
    private String email;
    private String password;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private UserGender gender;
}
