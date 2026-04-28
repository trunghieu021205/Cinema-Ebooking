package com.cinemaebooking.backend.common.validation.domain;

import com.cinemaebooking.backend.common.validation.builder.StringValidationBuilder;
import com.cinemaebooking.backend.common.validation.engine.ValidationRule;
import com.cinemaebooking.backend.common.validation.patterns.ValidationPatterns;

import java.util.List;

public class GenreValidationProfile {

    public static final GenreValidationProfile INSTANCE = new GenreValidationProfile();

    private GenreValidationProfile() {}

    public List<ValidationRule<String>> nameRules() {
        return StringValidationBuilder.create()
                .notBlank()
                .length(2, 100)
                .pattern(ValidationPatterns.GENRE_NAME, "contains invalid characters")
                .containsLetter()
                .build();
    }
}