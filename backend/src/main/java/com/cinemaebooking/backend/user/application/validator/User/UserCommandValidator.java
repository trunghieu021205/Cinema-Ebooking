package com.cinemaebooking.backend.user.application.validator.User;

import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.common.validation.engine.ValidationEngine;
import com.cinemaebooking.backend.common.validation.factory.ValidationFactory;
import com.cinemaebooking.backend.user.application.dto.ChangeDTO.ChangePasswordRequest;
import com.cinemaebooking.backend.user.application.dto.UserDTO.AdminUpdateUserRequest;
import com.cinemaebooking.backend.user.application.dto.UserDTO.CreateUserRequest;
import com.cinemaebooking.backend.user.application.dto.UserDTO.UpdateUserRequest;
import com.cinemaebooking.backend.user.application.port.UserRepository;
import com.cinemaebooking.backend.user.domain.valueObject.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserCommandValidator {

    private final UserRepository userRepository;

    // ================== CREATE ==================
    public void validateCreateRequest(CreateUserRequest request) {

        if (request == null) {
            throw CommonExceptions.invalidInput("Request must not be null");
        }

        var profile = ValidationFactory.user();

        ValidationEngine engine = ValidationEngine.of()
                // ================== PHASE 1: FORMAT ==================
                .validate(request.getFullName(), "fullName", profile.fullNameRules())
                .validate(request.getEmail(), "email", profile.emailRules())
                .validate(request.getPhoneNumber(), "phoneNumber", profile.phoneRules())
                .validate(request.getDateOfBirth(), "dateOfBirth", profile.dobRules())
                .validate(request.getGender(), "gender", profile.genderRules())
                .validate(request.getPassword(), "password", profile.passwordRules());

        // nếu format fail → stop, không query DB
        if (engine.hasErrors()) {
            engine.throwIfInvalid();
            return;
        }

        // ================== PHASE 2: BUSINESS ==================
        engine
                .validateUnique(
                        request.getEmail(),
                        "email",
                        this::emailExists
                )
                .throwIfInvalid();
    }

    // ================== UPDATE (USER SELF) ==================
    public void validateUpdateRequest(UserId id, UpdateUserRequest request) {

        if (id == null || request == null) {
            throw CommonExceptions.invalidInput("User id and request must not be null");
        }

        var profile = ValidationFactory.user();

        ValidationEngine.of()
                .validate(request.getFullName(), "fullName", profile.fullNameRules())
                .validate(request.getPhoneNumber(), "phoneNumber", profile.phoneRules())
                .throwIfInvalid();
    }

    // ================== UPDATE (ADMIN) ==================
    public void validateAdminUpdateRequest(UserId id, AdminUpdateUserRequest request) {

        if (id == null || request == null) {
            throw CommonExceptions.invalidInput("User id and request must not be null");
        }

        var profile = ValidationFactory.user();

        ValidationEngine.of()
                .validate(request.getFullName(), "fullName", profile.fullNameRules())
                .validate(request.getPhoneNumber(), "phoneNumber", profile.phoneRules())
                .validate(request.getRole(), "role", profile.roleRules())
                .validate(request.getStatus(), "status", profile.statusRules())
                .throwIfInvalid();
    }

    // ================== CHANGE PASSWORD ==================
    public void validateChangePasswordRequest(UserId id, ChangePasswordRequest request) {

        if (id == null || request == null) {
            throw CommonExceptions.invalidInput("User id and request must not be null");
        }

        var profile = ValidationFactory.user();

        ValidationEngine.of()
                .validate(request.getNewPassword(), "password", profile.passwordRules())
                .throwIfInvalid();
    }

    // ================== BUSINESS CHECKS ==================

    private boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }
}