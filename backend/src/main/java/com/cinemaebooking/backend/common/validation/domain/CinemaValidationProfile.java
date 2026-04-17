package com.cinemaebooking.backend.common.validation.domain;

import com.cinemaebooking.backend.common.validation.builder.ValidationBuilder;
import com.cinemaebooking.backend.common.validation.engine.ValidationRule;
import com.cinemaebooking.backend.common.validation.patterns.ValidationPatterns;

import java.util.List;

/**
 * CinemaValidationProfile - Collection of validation rule sets for Cinema domain.
 * Responsibility:
 * - Provide predefined validation rule groups for Cinema-related fields
 * - Ensure consistency of validation across Cinema use cases (create/update)
 * - Centralize validation logic for cinema domain inputs
 *
 * @author Hieu Nguyen
 * @since 2026
 */
public class CinemaValidationProfile {

    public static final CinemaValidationProfile INSTANCE =
            new CinemaValidationProfile();

    private CinemaValidationProfile() {}

    public List<ValidationRule<String>> nameRules() {
        return ValidationBuilder.create()
                .notBlank()
                .length(3, 80)
                .pattern(
                        ValidationPatterns.CINEMA_NAME,
                        "contains invalid characters"
                )
                .containsLetter()
                .build();
    }

    public List<ValidationRule<String>> cityRules() {
        return ValidationBuilder.create()
                .notBlank()
                .length(2, 50)
                .pattern(
                        ValidationPatterns.CITY,
                        "contains invalid characters"
                )
                .containsLetter()
                .build();
    }

    public List<ValidationRule<String>> addressRules() {
        return ValidationBuilder.create()
                .notBlank()
                .length(5, 150)
                .pattern(
                        ValidationPatterns.ADDRESS,
                        "format is invalid"
                )
                .build();
    }
}