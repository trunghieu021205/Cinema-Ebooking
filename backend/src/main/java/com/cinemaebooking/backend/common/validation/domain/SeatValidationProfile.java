package com.cinemaebooking.backend.common.validation.domain;

import com.cinemaebooking.backend.common.exception.ErrorCategory;
import com.cinemaebooking.backend.common.exception.ErrorDetail;
import com.cinemaebooking.backend.common.validation.builder.StringValidationBuilder;
import com.cinemaebooking.backend.common.validation.builder.ValidationBuilder;
import com.cinemaebooking.backend.common.validation.engine.ValidationRule;
import com.cinemaebooking.backend.common.validation.patterns.ValidationPatterns;
import com.cinemaebooking.backend.seat.domain.enums.SeatStatus;

import java.util.List;
import java.util.Optional;

/**
 * SeatValidationProfile - Validation rules for Seat domain.
 */
public class SeatValidationProfile {

    public static final SeatValidationProfile INSTANCE =
            new SeatValidationProfile();

    private SeatValidationProfile() {}

    // ================== ROW LABEL ==================

    public List<ValidationRule<String>> rowLabelRules() {
        return StringValidationBuilder.create()
                .notBlank()
                .length(1, 3)
                .pattern(
                        ValidationPatterns.ROW_LABEL,
                        "INVALID_ROW_LABEL"
                )
                .build();
    }

    // ================== COLUMN NUMBER ==================

    public List<ValidationRule<Integer>> columnNumberRules() {
        return ValidationBuilder.<Integer>create()
                .notNull()
                .custom(context -> {
                    Integer value = context.getValue();

                    if (value != null && value <= 0) {
                        return Optional.of(
                                new ErrorDetail(
                                        context.getField(),
                                        ErrorCategory.INVALID_VALUE,
                                        "columnNumber must be greater than 0"
                                )
                        );
                    }
                    return Optional.empty();
                })
                .build();
    }

    // ================== STATUS ==================

    public List<ValidationRule<SeatStatus>> statusRules() {
        return ValidationBuilder.<SeatStatus>create()
                .notNull()
                .build();
    }

    // ================== ROOM ID ==================

    public List<ValidationRule<Long>> roomIdRules() {
        return ValidationBuilder.<Long>create()
                .notNull()
                .custom(context -> {
                    Long value = context.getValue();

                    if (value != null && value <= 0) {
                        return Optional.of(
                                new ErrorDetail(
                                        context.getField(),
                                        ErrorCategory.INVALID_VALUE,
                                        "roomId must be positive"
                                )
                        );
                    }
                    return Optional.empty();
                })
                .build();
    }

    // ================== SEAT TYPE ID ==================

    public List<ValidationRule<Long>> seatTypeIdRules() {
        return ValidationBuilder.<Long>create()
                .notNull()
                .custom(context -> {
                    Long value = context.getValue();

                    if (value != null && value <= 0) {
                        return Optional.of(
                                new ErrorDetail(
                                        context.getField(),
                                        ErrorCategory.INVALID_VALUE,
                                        "seatTypeId must be positive"
                                )
                        );
                    }
                    return Optional.empty();
                })
                .build();
    }
}