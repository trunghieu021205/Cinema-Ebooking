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

    public List<ValidationRule> nameRules() {
        return ValidationBuilder.create()
                .notBlank()
                .length(3, 80)
                .pattern(
                        ValidationPatterns.CINEMA_NAME,
                        "cinema name contains invalid characters"
                )
                .build();
    }

    public List<ValidationRule> cityRules() {
        return ValidationBuilder.create()
                .notBlank()
                .length(2, 100)
                .pattern(
                        ValidationPatterns.CITY,
                        "city contains invalid characters"
                )
                .build();
    }

    public List<ValidationRule> addressRules() {
        return ValidationBuilder.create()
                .notBlank()
                .length(5, 150)
                .pattern(
                        ValidationPatterns.ADDRESS,
                        "address format is invalid"
                )
                .build();
    }
}