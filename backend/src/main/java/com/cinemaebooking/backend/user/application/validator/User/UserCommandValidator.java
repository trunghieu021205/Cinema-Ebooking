package com.cinemaebooking.backend.user.application.validator.User;

import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.common.exception.domain.UserExceptions;
import com.cinemaebooking.backend.common.validation.engine.ValidationEngine;
import com.cinemaebooking.backend.common.validation.factory.ValidationFactory;
import com.cinemaebooking.backend.user.application.dto.ChangeDTO.ChangePasswordRequest;
import com.cinemaebooking.backend.user.application.dto.UserDTO.AdminUpdateUserRequest;
import com.cinemaebooking.backend.user.application.dto.UserDTO.CreateUserRequest;
import com.cinemaebooking.backend.user.application.dto.UserDTO.UpdateUserRequest;
import com.cinemaebooking.backend.user.application.port.UserRepository;
import com.cinemaebooking.backend.user.domain.valueObject.UserGender;
import com.cinemaebooking.backend.user.domain.valueObject.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class UserCommandValidator {

    private final UserRepository userRepository;

    // ================== CREATE ==================
    public void validateCreateRequest(CreateUserRequest request) {
        if (request == null) {
            throw CommonExceptions.invalidInput("Request must not be null");
        }

        validateBaseFields(
                request.getFullName(),
                request.getEmail(),
                request.getPhoneNumber(),
                request.getDateOfBirth(),
                request.getGender()
        );

        validatePasswordField(request.getPassword());

        validateDuplicateEmail(request.getEmail(), null);
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

        validatePasswordField(request.getNewPassword());
    }

    // ================== FIELD VALIDATION ==================

    private void validateBaseFields(String fullName, String email, String phone, LocalDate dateOfBirth, UserGender gender) {
        var profile = ValidationFactory.user();

        ValidationEngine.of()
                .validate(fullName, "fullName", profile.fullNameRules())
                .validate(email, "email", profile.emailRules())
                .validate(dateOfBirth, "dateOfBirth", profile.dobRules())
                .validate(gender, "gender", profile.genderRules())
                .validate(phone, "phoneNumber", profile.phoneRules())
                .throwIfInvalid();
    }

    private void validatePasswordField(String password) {
        var profile = ValidationFactory.user();
        ValidationEngine.of()
                .validate(password, "password", profile.passwordRules())
                .throwIfInvalid();
    }

    // ================== BUSINESS RULES ==================

    private void validateDuplicateEmail(String email, UserId id) {
        if (email == null) return;

        if (id == null) {
            if (userRepository.existsByEmail(email)) {
                throw UserExceptions.duplicateEmail(email);
            }
        } else {
            if (userRepository.existsByEmailAndIdNot(email, id)) {
                throw UserExceptions.duplicateEmail(email);
            }
        }
    }
}