package com.cinemaebooking.backend.common.validation.domain;

import com.cinemaebooking.backend.common.validation.builder.ValidationBuilder;
import com.cinemaebooking.backend.common.validation.engine.ValidationRule;
import com.cinemaebooking.backend.common.validation.patterns.ValidationPatterns;

import java.util.List;

/**
 * SeatTypeValidationProfile - Validation rules for SeatType domain.
 */
public class SeatTypeValidationProfile {

    public static final SeatTypeValidationProfile INSTANCE =
            new SeatTypeValidationProfile();

    private SeatTypeValidationProfile() {}

    // ================== NAME ==================

    public List<ValidationRule<String>> nameRules() {
        return ValidationBuilder.create()
                .notBlank()
                .length(2, 50)
                .pattern(
                        ValidationPatterns.SEAT_TYPE_NAME,
                        "contains invalid characters"
                )
                .containsLetter()
                .build();
    }

    // ================== BASE PRICE ==================

    public List<ValidationRule<Long>> basePriceRules() {
        return List.of(
                context -> {
                    Long value = context.value();

                    if (value == null) {
                        throw new RuntimeException("Base price must not be null");
                    }
                    if (value <= 0) {
                        throw new RuntimeException("Base price must be positive");
                    }
                }
        );
    }
}