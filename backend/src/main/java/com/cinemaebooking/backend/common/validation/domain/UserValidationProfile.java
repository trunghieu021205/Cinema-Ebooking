package com.cinemaebooking.backend.common.validation.domain;

import com.cinemaebooking.backend.common.exception.domain.UserExceptions;
import com.cinemaebooking.backend.common.validation.builder.ValidationBuilder;
import com.cinemaebooking.backend.common.validation.engine.ValidationRule;
import com.cinemaebooking.backend.common.validation.patterns.ValidationPatterns;

import java.util.List;

/**
 * UserValidationProfile - Collection of validation rule sets for User domain.
 */
public class UserValidationProfile {

    public static final UserValidationProfile INSTANCE =
            new UserValidationProfile();

    private UserValidationProfile() {}

    // ================== FULL NAME ==================

    public List<ValidationRule<String>> fullNameRules() {
        return ValidationBuilder.create()
                .notBlank()
                .length(2, 100)
                .pattern(
                        ValidationPatterns.FULLNAME,
                        "chứa kí tự không hợp lệ"
                )
                .containsLetter()
                .build();
    }

    // ================== EMAIL ==================

    public List<ValidationRule<String>> emailRules() {
        return ValidationBuilder.create()
                .notBlank()
                .length(5, 150)
                .pattern(
                        ValidationPatterns.EMAIL,
                        "không đúng định dạng"
                )
                .build();
    }

    // ================== PASSWORD ==================

    public List<ValidationRule<String>> passwordRules() {
        return ValidationBuilder.create()
                .notBlank()
                .length(6, 100)
                .build();
    }

    // ================== DATEOFBIRTH ==================

    public List<ValidationRule<Object>> dobRules() {
        return List.of(
                value -> {
                    if (value == null) {
                        throw UserExceptions.invalidDOB();
                    }
                }
        );
    }

    // ================== GENDER ==================

    public List<ValidationRule<Object>> genderRules() {
        return List.of(
                value -> {
                    if (value == null) {
                        throw UserExceptions.invalidGender();
                    }
                }
        );
    }


    // ================== PHONE ==================

    public List<ValidationRule<String>> phoneRules() {
        return ValidationBuilder.create()
                .notBlank()
                .length(7,15)
                .pattern(
                        ValidationPatterns.PHONE,
                        "không đúng định dạng"
                )
                .build();
    }

    // ================== ROLE ==================

    public List<ValidationRule<Object>> roleRules() {
        return List.of(
                value -> {
                    if (value == null) {
                        throw UserExceptions.invalidRole();
                    }
                }
        );
    }

    // ================== STATUS ==================

    public List<ValidationRule<Object>> statusRules() {
        return List.of(
                value -> {
                    if (value == null) {
                        throw UserExceptions.invalidStatus();
                    }
                }
        );
    }
}