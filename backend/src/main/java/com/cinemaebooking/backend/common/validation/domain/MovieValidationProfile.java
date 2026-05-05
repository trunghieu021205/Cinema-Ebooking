package com.cinemaebooking.backend.common.validation.domain;

import com.cinemaebooking.backend.common.exception.ErrorCategory;
import com.cinemaebooking.backend.common.exception.ErrorDetail;
import com.cinemaebooking.backend.common.validation.builder.StringValidationBuilder;
import com.cinemaebooking.backend.common.validation.builder.ValidationBuilder;
import com.cinemaebooking.backend.common.validation.engine.ValidationRule;
import com.cinemaebooking.backend.common.validation.patterns.ValidationPatterns;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class MovieValidationProfile {

    public static final MovieValidationProfile INSTANCE = new MovieValidationProfile();

    private MovieValidationProfile() {}

    // ================== TITLE ==================

    public List<ValidationRule<String>> titleRules() {
        return StringValidationBuilder.create()
                .notBlank()
                .length(5, 200)
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
                .length(5, 2000)
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
                    if (value != null && value > 600) {   // 600 phút = 10 giờ, quá dài cho 1 bộ phim
                        return Optional.of(
                                new ErrorDetail(
                                        context.getField(),
                                        ErrorCategory.INVALID_VALUE,
                                        "duration must not exceed 600 minutes"
                                )
                        );
                    }
                    return Optional.empty();
                })
                .build();
    }

    // ================== RELEASE DATE ==================

    public List<ValidationRule<LocalDate>> releaseDateRules() {
        return ValidationBuilder.<LocalDate>create()
                .notNull()
                .custom(context -> {
                    LocalDate value = context.getValue();
                    if (value == null) return Optional.empty();
                    LocalDate now = LocalDate.now();
                    // Không cho phép ngày phát hành quá 100 năm trước hoặc quá 10 năm sau
                    if (value.isBefore(now.minusYears(100))) {
                        return Optional.of(
                                new ErrorDetail(
                                        context.getField(),
                                        ErrorCategory.INVALID_VALUE,
                                        "release date cannot be more than 100 years in the past"
                                )
                        );
                    }
                    if (value.isAfter(now.plusYears(10))) {
                        return Optional.of(
                                new ErrorDetail(
                                        context.getField(),
                                        ErrorCategory.INVALID_VALUE,
                                        "release date cannot be more than 10 years in the future"
                                )
                        );
                    }
                    return Optional.empty();
                })
                .build();
    }
}