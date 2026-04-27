package com.cinemaebooking.backend.user.application.validator.Auth;

import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.common.exception.domain.UserExceptions;
import com.cinemaebooking.backend.common.validation.engine.ValidationEngine;
import com.cinemaebooking.backend.common.validation.factory.ValidationFactory;
import com.cinemaebooking.backend.user.application.dto.AuthDTO.RegisterRequest;
import com.cinemaebooking.backend.user.application.port.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegisterValidator {

    private final UserRepository userRepository;

    public void validate(RegisterRequest request) {

        if (request == null) {
            throw CommonExceptions.invalidInput("Register request must not be null");
        }

        var profile = ValidationFactory.user();

        // ================== FORMAT VALIDATION ==================

        ValidationEngine.of()
                .validate(request.getFullName(), "fullName", profile.fullNameRules())
                .validate(request.getEmail(), "email", profile.emailRules())
                .validate(request.getPhoneNumber(), "phoneNumber", profile.phoneRules())
                .validate(request.getDateOfBirth(), "dateOfBirth", profile.dobRules())
                .validate(request.getGender(), "gender", profile.genderRules())
                .validate(request.getPassword(), "password", profile.passwordRules())
                .throwIfInvalid();
        // ================== BUSINESS RULES ==================

        validateUniqueEmail(request.getEmail());
    }

    // ================== UNIQUE CHECKS ==================

    private void validateUniqueEmail(String email) {
        if (email == null) return;

        if (userRepository.existsByEmail(email)) {
            throw UserExceptions.duplicateEmail(email);
        }
    }
}
