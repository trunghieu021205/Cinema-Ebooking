package com.cinemaebooking.backend.common.validation.domain;

import com.cinemaebooking.backend.booking.domain.enums.BookingStatus;
import com.cinemaebooking.backend.common.exception.ErrorCategory;
import com.cinemaebooking.backend.common.exception.ErrorDetail;
import com.cinemaebooking.backend.common.validation.builder.ValidationBuilder;
import com.cinemaebooking.backend.common.validation.engine.ValidationRule;

import java.util.List;
import java.util.Optional;

public class BookingValidationProfile {

    public static final BookingValidationProfile INSTANCE = new BookingValidationProfile();

    private BookingValidationProfile() {}

    // ================== SHOWTIME ID ==================

    public List<ValidationRule<Long>> showtimeIdRules() {
        return ValidationBuilder.<Long>create()
                .notNull()
                .custom(context -> {
                    Long value = context.getValue();
                    if (value != null && value <= 0) {
                        return Optional.of(new ErrorDetail(
                                context.getField(),
                                ErrorCategory.INVALID_VALUE,
                                "showtimeId phải là số dương"
                        ));
                    }
                    return Optional.empty();
                })
                .build();
    }

    // ================== SEAT IDS (LIST) ==================

    public List<ValidationRule<List<Long>>> seatIdsRules() {
        return ValidationBuilder.<List<Long>>create()
                .notNull()
                .custom(context -> {
                    List<Long> value = context.getValue();
                    if (value == null || value.isEmpty()) {
                        return Optional.of(new ErrorDetail(
                                context.getField(),
                                ErrorCategory.REQUIRED,
                                "danh sách ghế không được để trống"
                        ));
                    }
                    if (value.size() > 8) {
                        return Optional.of(new ErrorDetail(
                                context.getField(),
                                ErrorCategory.INVALID_VALUE,
                                "không được đặt quá 8 ghế mỗi lần"
                        ));
                    }
                    return Optional.empty();
                })
                .build();
    }

    // ================== COMBO QUANTITY ==================

    public List<ValidationRule<Integer>> comboQuantityRules() {
        return ValidationBuilder.<Integer>create()
                .notNull()
                .custom(context -> {
                    Integer value = context.getValue();
                    if (value != null && (value < 1 || value > 20)) {
                        return Optional.of(new ErrorDetail(
                                context.getField(),
                                ErrorCategory.INVALID_VALUE,
                                "số lượng combo phải từ 1 đến 20"
                        ));
                    }
                    return Optional.empty();
                })
                .build();
    }

    // ================== BOOKING STATUS ==================

    public List<ValidationRule<BookingStatus>> statusRules() {
        return ValidationBuilder.<BookingStatus>create()
                .notNull()
                .build();
    }

    // ================== COUPON CODE ==================

    public List<ValidationRule<String>> couponCodeRules() {
        return ValidationBuilder.<String>create()
                .custom(context -> {
                    String value = context.getValue();
                    if (value != null && (value.trim().length() < 3 || value.trim().length() > 20)) {
                        return Optional.of(new ErrorDetail(
                                context.getField(),
                                ErrorCategory.INVALID_VALUE,
                                "mã giảm giá phải từ 3 đến 20 kí tự"
                        ));
                    }
                    return Optional.empty();
                })
                .build();
    }
}
