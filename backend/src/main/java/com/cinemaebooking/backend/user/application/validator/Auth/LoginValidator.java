package com.cinemaebooking.backend.user.application.validator.Auth;

import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.common.validation.engine.ValidationEngine;
import com.cinemaebooking.backend.common.validation.factory.ValidationFactory;
import com.cinemaebooking.backend.user.application.dto.AuthDTO.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoginValidator {

    public void validate(LoginRequest request) {

        if (request == null) {
            throw CommonExceptions.invalidInput("Login request must not be null");
        }

        var profile = ValidationFactory.user();

        // Email format check
        ValidationEngine.validate(request.getEmail(), "Email", profile.emailRules());

        // Password basic validation (format/length)
        ValidationEngine.validate(request.getPassword(), "Password", profile.passwordRules());
    }
}
