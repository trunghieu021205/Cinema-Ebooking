package com.cinemaebooking.backend.common.validation.domain;

import com.cinemaebooking.backend.common.validation.builder.StringValidationBuilder;
import com.cinemaebooking.backend.common.validation.engine.ValidationRule;
import com.cinemaebooking.backend.common.validation.patterns.ValidationPatterns;

import java.util.List;

public class ComboValidationProfile {

    public static final ComboValidationProfile INSTANCE = new ComboValidationProfile();

    private ComboValidationProfile() {}

    public List<ValidationRule<String>> nameRules() {
        return StringValidationBuilder.create()
                .notBlank()
                .length(3, 150)
                .pattern(ValidationPatterns.CINEMA_NAME, "contains invalid characters")
                .containsLetter()
                .build();
    }
}