package com.cinemaebooking.backend.common.validation.domain;

import com.cinemaebooking.backend.common.validation.builder.ValidationBuilder;
import com.cinemaebooking.backend.common.validation.engine.ValidationRule;
import com.cinemaebooking.backend.common.validation.patterns.ValidationPatterns;

import java.util.List;

/**
 * SeatValidationProfile - Validation rules for Seat domain.
 */
public class SeatValidationProfile {

    public static final SeatValidationProfile INSTANCE =
            new SeatValidationProfile();

    private SeatValidationProfile() {}

    // ================== ROW LABEL ==================

    public List<ValidationRule<String>> rowLabelRules() {
        return ValidationBuilder.create()
                .notBlank()
                .length(1, 3)
                .pattern(
                        ValidationPatterns.ROW_LABEL,
                        "must be uppercase letters (e.g., A, B, AA)"
                )
                .build();
    }

    // ================== COLUMN NUMBER ==================

    public List<ValidationRule<Integer>> columnNumberRules() {
        return List.of(
                context -> {
                    Integer value = context.value();

                    if (value == null) {
                        throw new RuntimeException("Column number must not be null");
                    }
                    if (value <= 0) {
                        throw new RuntimeException("Column number must be greater than 0");
                    }
                }
        );
    }

    // ================== STATUS ==================

    public List<ValidationRule<Object>> statusRules() {
        return List.of(
                context -> {
                    if (context.value() == null) {
                        throw new RuntimeException("Seat status must not be null");
                    }
                }
        );
    }

    // ================== ROOM ID ==================

    public List<ValidationRule<Long>> roomIdRules() {
        return List.of(
                context -> {
                    Long value = context.value();

                    if (value == null) {
                        throw new RuntimeException("Room id must not be null");
                    }
                    if (value <= 0) {
                        throw new RuntimeException("Room id must be positive");
                    }
                }
        );
    }

    // ================== SEAT TYPE ID ==================

    public List<ValidationRule<Long>> seatTypeIdRules() {
        return List.of(
                context -> {
                    Long value = context.value();

                    if (value == null) {
                        throw new RuntimeException("Seat type id must not be null");
                    }
                    if (value <= 0) {
                        throw new RuntimeException("Seat type id must be positive");
                    }
                }
        );
    }
}