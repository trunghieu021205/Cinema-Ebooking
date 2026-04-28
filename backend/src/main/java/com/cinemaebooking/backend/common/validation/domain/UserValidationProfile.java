package com.cinemaebooking.backend.common.validation.domain;

import com.cinemaebooking.backend.common.exception.ErrorCategory;
import com.cinemaebooking.backend.common.exception.ErrorDetail;
import com.cinemaebooking.backend.common.validation.builder.StringValidationBuilder;
import com.cinemaebooking.backend.common.validation.builder.ValidationBuilder;
import com.cinemaebooking.backend.common.validation.engine.ValidationRule;
import com.cinemaebooking.backend.common.validation.patterns.ValidationPatterns;
import com.cinemaebooking.backend.user.domain.enums.UserRole;
import com.cinemaebooking.backend.user.domain.enums.UserStatus;
import com.cinemaebooking.backend.user.domain.valueObject.UserGender;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class UserValidationProfile {

    public static final UserValidationProfile INSTANCE =
            new UserValidationProfile();

    private UserValidationProfile() {}

    // ================== FULL NAME ==================

    public List<ValidationRule<String>> fullNameRules() {
        return StringValidationBuilder.create()
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
        return StringValidationBuilder.create()
                .notBlank()
                .length(5, 150)
                .pattern(
                        ValidationPatterns.EMAIL,
                        "email không đúng định dạng"
                )
                .build();
    }

    // ================== PASSWORD ==================

    public List<ValidationRule<String>> passwordRules() {
        return StringValidationBuilder.create()
                .notBlank()
                .length(6, 100)
                .build();
    }

    // ================== DATE OF BIRTH ==================

    public List<ValidationRule<LocalDate>> dobRules() {
        return ValidationBuilder.<LocalDate>create()
                .notNull()
                .custom(context -> {
                    LocalDate dob = context.getValue();
                    if (dob != null && dob.isAfter(LocalDate.now())) {
                        return Optional.of(
                                new ErrorDetail(context.getField(), ErrorCategory.INVALID_VALUE, "ngày sinh không hợp lệ")
                        );
                    }
                    return Optional.empty();
                })
                .build();
    }

    // ================== GENDER ==================

    public List<ValidationRule<UserGender>> genderRules() {
        return ValidationBuilder.<UserGender>create()
                .notNull()
                .build();
    }

    // ================== PHONE ==================

    public List<ValidationRule<String>> phoneRules() {
        return StringValidationBuilder.create()
                .notBlank()
                .length(7, 15)
                .pattern(
                        ValidationPatterns.PHONE,
                        "Số điện thoại không đúng định dạng"
                )
                .build();
    }

    // ================== ROLE ==================

    public List<ValidationRule<UserRole>> roleRules() {
        return ValidationBuilder.<UserRole>create()
                .notNull()
                .build();
    }

    // ================== STATUS ==================

    public List<ValidationRule<UserStatus>> statusRules() {
        return ValidationBuilder.<UserStatus>create()
                .notNull()
                .build();
    }
}