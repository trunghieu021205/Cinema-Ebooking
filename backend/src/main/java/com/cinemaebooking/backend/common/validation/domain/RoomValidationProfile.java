package com.cinemaebooking.backend.common.validation.domain;

import com.cinemaebooking.backend.common.exception.ErrorCategory;
import com.cinemaebooking.backend.common.exception.ErrorDetail;
import com.cinemaebooking.backend.common.validation.builder.StringValidationBuilder;
import com.cinemaebooking.backend.common.validation.builder.ValidationBuilder;
import com.cinemaebooking.backend.common.validation.engine.ValidationRule;
import com.cinemaebooking.backend.common.validation.patterns.ValidationPatterns;
import com.cinemaebooking.backend.room.domain.enums.RoomType;

import java.util.List;
import java.util.Optional;

/**
 * RoomValidationProfile - Collection of validation rule sets for Room domain.
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
                        "contains invalid characters"
                )
                .containsLetter()
                .build();
    }

    // ================== TOTAL SEATS ==================

    public List<ValidationRule<Integer>> capacityRules() {
        return ValidationBuilder.<Integer>create()
                .notNull()
                .custom(context -> {
                    Integer value = context.getValue();
                    if (value != null && value <= 0) {
                        return Optional.of(
                                new ErrorDetail(
                                        context.getField(),
                                        ErrorCategory.INVALID_VALUE,
                                        "capacity must be greater than 0"
                                )
                        );
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

// ================== CINEMA ID ==================

    public List<ValidationRule<Long>> cinemaIdRules() {
        return ValidationBuilder.<Long>create()
                .notNull()
                .custom(context -> {
                    Long value = context.getValue();
                    if (value != null && value <= 0) {
                        return Optional.of(
                                new ErrorDetail(
                                        context.getField(),
                                        ErrorCategory.INVALID_VALUE,
                                        "cinemaId must be positive"
                                )
                        );
                    }
                    return Optional.empty();
                })
                .build();
    }
}