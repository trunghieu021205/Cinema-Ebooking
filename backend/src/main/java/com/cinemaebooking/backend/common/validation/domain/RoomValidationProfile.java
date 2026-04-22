package com.cinemaebooking.backend.common.validation.domain;

import com.cinemaebooking.backend.common.exception.domain.RoomExceptions;
import com.cinemaebooking.backend.common.validation.builder.ValidationBuilder;
import com.cinemaebooking.backend.common.validation.engine.ValidationRule;
import com.cinemaebooking.backend.common.validation.patterns.ValidationPatterns;

import java.util.List;

/**
 * RoomValidationProfile - Collection of validation rule sets for Room domain.
 */
public class RoomValidationProfile {

    public static final RoomValidationProfile INSTANCE =
            new RoomValidationProfile();

    private RoomValidationProfile() {}

    // ================== NAME ==================

    public List<ValidationRule<String>> nameRules() {
        return ValidationBuilder.create()
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
        return List.of(
                context -> {
                    Integer value = context.value(); // ✅ lấy value đúng cách
                    if (value == null) {
                        throw RoomExceptions.invalidCapacity(value);
                    }
                    if (value <= 0) {
                        throw RoomExceptions.invalidCapacity(value);
                    }
                }
        );
    }

    // ================== ROOM TYPE ==================

    public List<ValidationRule<Object>> typeRules() {
        return List.of(
                value -> {
                    if (value == null) {
                        throw new RuntimeException("Room type must not be null");
                    }
                }
        );
    }

    // ================== CINEMA ID ==================

    public List<ValidationRule<Long>> cinemaIdRules() {
        return List.of(
                context -> {
                    Long value = context.value(); // ✅ lấy value đúng cách

                    if (value == null) {
                        throw new RuntimeException("Cinema id must not be null");
                    }
                    if (value <= 0) {
                        throw new RuntimeException("Cinema id must be positive");
                    }
                }
        );
    }
}