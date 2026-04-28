package com.cinemaebooking.backend.common.validation.domain;

import com.cinemaebooking.backend.common.exception.ErrorCategory;
import com.cinemaebooking.backend.common.exception.ErrorDetail;
import com.cinemaebooking.backend.common.validation.builder.StringValidationBuilder;
import com.cinemaebooking.backend.common.validation.builder.ValidationBuilder;
import com.cinemaebooking.backend.common.validation.engine.ValidationRule;
import com.cinemaebooking.backend.common.validation.patterns.ValidationPatterns;

import java.util.List;
import java.util.Optional;

/**
 * SeatTypeValidationProfile - Validation rules for SeatType domain.
 */
public class SeatTypeValidationProfile {

    public static final SeatTypeValidationProfile INSTANCE =
            new SeatTypeValidationProfile();

    private SeatTypeValidationProfile() {}

    // ================== NAME ==================

    public List<ValidationRule<String>> nameRules() {
        return StringValidationBuilder.create()
                .notBlank()
                .length(2, 50)
                .pattern(
                        ValidationPatterns.SEAT_TYPE_NAME,
                        "INVALID_SEAT_TYPE_NAME"
                )
                .containsLetter()
                .build();
    }

    // ================== BASE PRICE ==================

    public List<ValidationRule<Long>> basePriceRules() {
        return ValidationBuilder.<Long>create()
                .notNull()
                .custom(context -> {
                    Long value = context.getValue();

                    if (value != null && value <= 0) {
                        return Optional.of(
                                new ErrorDetail(
                                        context.getField(),
                                        ErrorCategory.INVALID_VALUE,
                                        "basePrice must be positive"
                                )
                        );
                    }

                    return Optional.empty();
                })
                .build();
    }
}