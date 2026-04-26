package com.cinemaebooking.backend.common.validation.domain;

import com.cinemaebooking.backend.common.validation.builder.ValidationBuilder;
import com.cinemaebooking.backend.common.validation.engine.ValidationRule;
import com.cinemaebooking.backend.common.validation.patterns.ValidationPatterns;

import java.util.List;

public class MovieValidationProfile {

    public static final MovieValidationProfile INSTANCE = new MovieValidationProfile();

    private MovieValidationProfile() {}

    public List<ValidationRule<String>> titleRules() {
        return ValidationBuilder.create()
                .notBlank()
                .length(1, 255)
                .pattern(ValidationPatterns.MOVIE_TITLE, "contains invalid characters")
                .build();
    }

    public List<ValidationRule<String>> descriptionRules() {
        return ValidationBuilder.create()
                .notNull()
                .length(0, 1000)
                .build();
    }

    public List<ValidationRule<String>> durationRules() {
        return ValidationBuilder.create()
                .notNull()
                .custom((ctx) -> {
                    String val = ctx.trimmed();
                    if (val != null) {
                        try {
                            int d = Integer.parseInt(val);
                            if (d <= 0) throw new IllegalArgumentException();
                        } catch (Exception e) {
                            throw com.cinemaebooking.backend.common.exception.domain.CommonExceptions.invalidInput(
                                    ctx.fieldName() + " must be a positive integer");
                        }
                    }
                })
                .build();
    }
}