package com.cinemaebooking.backend.common.validation.domain;

import com.cinemaebooking.backend.common.exception.ErrorCategory;
import com.cinemaebooking.backend.common.exception.ErrorDetail;
import com.cinemaebooking.backend.common.validation.builder.StringValidationBuilder;
import com.cinemaebooking.backend.common.validation.builder.ValidationBuilder;
import com.cinemaebooking.backend.common.validation.engine.ValidationRule;
import com.cinemaebooking.backend.common.validation.patterns.ValidationPatterns;

import java.util.List;
import java.util.Optional;

public class MovieValidationProfile {

    public static final MovieValidationProfile INSTANCE = new MovieValidationProfile();

    private MovieValidationProfile() {}

    // ================== TITLE ==================

    public List<ValidationRule<String>> titleRules() {
        return StringValidationBuilder.create()
                .notBlank()
                .length(1, 255)
                .pattern(
                        ValidationPatterns.MOVIE_TITLE,
                        "INVALID_TITLE"
                )
                .build();
    }

    // ================== DESCRIPTION ==================

    public List<ValidationRule<String>> descriptionRules() {
        return StringValidationBuilder.create()
                .notBlank()
                .length(10, 1000)
                .build();
    }

    // ================== DURATION ==================

    public List<ValidationRule<Integer>> durationRules() {
        return ValidationBuilder.<Integer>create()
                .notNull()
                .custom(context -> {
                    Integer value = context.getValue();
                    if (value != null && value <= 0) {
                        return Optional.of(
                                new ErrorDetail(
                                        context.getField(),
                                        ErrorCategory.INVALID_VALUE,
                                        "duration must be a positive number"
                                )
                        );
                    }
                    return Optional.empty();
                })
                .build();
    }
}