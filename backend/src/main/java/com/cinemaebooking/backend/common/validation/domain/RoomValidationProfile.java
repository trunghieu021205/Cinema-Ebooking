package com.cinemaebooking.backend.common.validation.domain;

import com.cinemaebooking.backend.common.exception.ErrorCategory;
import com.cinemaebooking.backend.common.exception.ErrorDetail;
import com.cinemaebooking.backend.common.validation.builder.StringValidationBuilder;
import com.cinemaebooking.backend.common.validation.builder.ValidationBuilder;
import com.cinemaebooking.backend.common.validation.engine.ValidationRule;
import com.cinemaebooking.backend.common.validation.patterns.ValidationPatterns;
import com.cinemaebooking.backend.room.domain.enums.RoomStatus;
import com.cinemaebooking.backend.room.domain.enums.RoomType;

import java.util.List;
import java.util.Optional;

/**
 * RoomValidationProfile - Collection of validation rule sets for Room domain.
 * Responsibility:
 * - Provide predefined validation rule groups for Room-related fields
 * - Ensure consistency of validation across Room use cases (create/update)
 * - Centralize validation logic for room domain inputs
 *
 * @author Hieu Nguyen
 * @since 2026
 */
public class RoomValidationProfile {

    public static final RoomValidationProfile INSTANCE =
            new RoomValidationProfile();

    private RoomValidationProfile() {}

    // ================== NAME ==================

    public List<ValidationRule<String>> nameRules() {
        return StringValidationBuilder.create()
                .notBlank()
                .length(2, 50)
                .pattern(
                        ValidationPatterns.ROOM_NAME,
                        "chứa kí tự không hợp lệ"
                )
                .containsLetter()
                .build();
    }

    // ================== ROWS ==================

    public List<ValidationRule<Integer>> numberOfRowsRules() {
        return ValidationBuilder.<Integer>create()
                .notNull()
                .custom(context -> {
                    Integer value = context.getValue();
                    if (value != null && (value < 1 || value > 26)) {
                        return Optional.of(new ErrorDetail(
                                context.getField(),
                                ErrorCategory.INVALID_VALUE,
                                "rows phải là số từ 1 đến 26"
                        ));
                    }
                    return Optional.empty();
                })
                .build();
    }

    // ================== COLS ==================

    public List<ValidationRule<Integer>> numberOfColsRules() {
        return ValidationBuilder.<Integer>create()
                .notNull()
                .custom(context -> {
                    Integer value = context.getValue();
                    if (value != null && (value < 1 || value > 50)) {
                        return Optional.of(new ErrorDetail(
                                context.getField(),
                                ErrorCategory.INVALID_VALUE,
                                "cols phải là số từ 1 đến 50"
                        ));
                    }
                    return Optional.empty();
                })
                .build();
    }

    // ================== ROOM TYPE ==================

    public List<ValidationRule<RoomType>> typeRules() {
        return ValidationBuilder.<RoomType>create()
                .notNull()
                .build();
    }

    // ================== ROOM STATUS ==================

    public List<ValidationRule<RoomStatus>> statusRules() {
        return ValidationBuilder.<RoomStatus>create()
                .notNull()
                .build();
    }

    // ================== CINEMA ID ==================

    public List<ValidationRule<Long>> cinemaIdRules() {
        return ValidationBuilder.<Long>create()
                .notNull()
                .custom(context -> {
                    Long value = context.getValue();
                    if (value != null && value <= 0) {
                        return Optional.of(new ErrorDetail(
                                context.getField(),
                                ErrorCategory.INVALID_VALUE,
                                "cinemaId phải lớn hơn 0"
                        ));
                    }
                    return Optional.empty();
                })
                .build();
    }
}