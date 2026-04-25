package com.cinemaebooking.backend.common.validation.domain;

import com.cinemaebooking.backend.common.validation.builder.ValidationBuilder;
import com.cinemaebooking.backend.common.validation.engine.ValidationRule;

import java.util.List;

public class CouponValidationProfile {

    public static final CouponValidationProfile INSTANCE = new CouponValidationProfile();

    private CouponValidationProfile() {}

    public List<ValidationRule<String>> codeRules() {
        return ValidationBuilder.create()
                .notBlank()
                .length(3, 100)
                .pattern("^[A-Z0-9_-]+$", "must contain only uppercase letters, numbers, underscore or hyphen")
                .build();
    }
}