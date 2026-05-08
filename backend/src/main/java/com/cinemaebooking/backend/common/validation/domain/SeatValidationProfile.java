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
 * SeatValidationProfile - Collection of validation rule sets for Seat domain.
 * Responsibility:
 * - Provide predefined validation rule groups for Seat-related fields
 * - Ensure consistency of validation across Seat use cases (create/update)
 * - Centralize validation logic for seat domain inputs
 *
 * @author Hieu Nguyen
 * @since 2026
 */
public class SeatValidationProfile {

    public static final SeatValidationProfile INSTANCE =
            new SeatValidationProfile();

    private SeatValidationProfile() {}

    // ================== ROW INDEX ==================

    public List<ValidationRule<Integer>> rowIndexRules() {
        return ValidationBuilder.<Integer>create()
                .notNull()
                .custom(context -> {
                    Integer value = context.getValue();
                    if (value != null && value < 0) {
                        return Optional.of(new ErrorDetail(
                                context.getField(),
                                ErrorCategory.INVALID_VALUE,
                                "rowIndex must not be negative"
                        ));
                    }
                    return Optional.empty();
                })
                .build();
    }

    // ================== COL INDEX ==================

    public List<ValidationRule<Integer>> colIndexRules() {
        return ValidationBuilder.<Integer>create()
                .notNull()
                .custom(context -> {
                    Integer value = context.getValue();
                    if (value != null && value < 0) {
                        return Optional.of(new ErrorDetail(
                                context.getField(),
                                ErrorCategory.INVALID_VALUE,
                                "colIndex must not be negative"
                        ));
                    }
                    return Optional.empty();
                })
                .build();
    }

    // ================== LABEL ==================

    public List<ValidationRule<String>> labelRules() {
        return StringValidationBuilder.create()
                .notBlank()
                .length(2, 3)
                .pattern(
                        ValidationPatterns.SEAT_LABEL,
                        "contains invalid characters"
                )
                .build();
    }

    // ================== SEAT TYPE ID ==================

    // nullable khi mới generate layout — chỉ validate khi update
    public List<ValidationRule<Long>> seatTypeIdRules() {
        return ValidationBuilder.<Long>create()
                .notNull()
                .custom(context -> {
                    Long value = context.getValue();
                    if (value != null && value <= 0) {
                        return Optional.of(new ErrorDetail(
                                context.getField(),
                                ErrorCategory.INVALID_VALUE,
                                "seatTypeId must be positive"
                        ));
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
                        return Optional.of(new ErrorDetail(
                                context.getField(),
                                ErrorCategory.INVALID_VALUE,
                                "roomId must be positive"
                        ));
                    }
                    return Optional.empty();
                })
                .build();
    }
}