package com.cinemaebooking.backend.user.application.validator.Auth;

import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
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

        // ================== PHASE 1: FORMAT VALIDATION ==================

        ValidationEngine engine = ValidationEngine.of()
                .validate(request.getFullName(), "fullName", profile.fullNameRules())
                .validate(request.getEmail(), "email", profile.emailRules())
                .validate(request.getPhoneNumber(), "phoneNumber", profile.phoneRules())
                .validate(request.getDateOfBirth(), "dateOfBirth", profile.dobRules())
                .validate(request.getGender(), "gender", profile.genderRules())
                .validate(request.getPassword(), "password", profile.passwordRules());

        // nếu format lỗi → dừng luôn (KHÔNG query DB)
        if (engine.hasErrors()) {
            engine.throwIfInvalid();
            return;
        }

        // ================== PHASE 2: BUSINESS VALIDATION ==================

        engine
                .validateUnique(
                        request.getEmail(),
                        "email",
                        this::emailExists
                )
                .validateUnique(
                        request.getPhoneNumber(),
                        "phoneNumber",
                        this::phoneExists
                )
                .throwIfInvalid();
    }

    // ================== DB CHECK (RETURN TRUE IF EXISTS) ==================

    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean phoneExists(String phoneNumber) {
        return userRepository.existsByPhoneNumber(phoneNumber);
    }
}