package com.cinemaebooking.backend.common.validation.domain;

import com.cinemaebooking.backend.common.exception.ErrorCategory;
import com.cinemaebooking.backend.common.exception.ErrorDetail;
import com.cinemaebooking.backend.common.validation.engine.ValidationRule;

import java.util.List;
import java.util.Optional;

public final class UserCouponValidationProfile {

    public static final UserCouponValidationProfile INSTANCE = new UserCouponValidationProfile();

    private UserCouponValidationProfile() {}

    /**
     * userId: không được null và phải > 0
     */
    public List<ValidationRule<Long>> userIdRules() {
        return List.of(
                context -> {
                    Long value = context.value();
                    if (value == null) {
                        return Optional.of(new ErrorDetail(
                                context.fieldName(),
                                ErrorCategory.REQUIRED,
                                "không được để trống"
                        ));
                    }
                    if (value <= 0) {
                        return Optional.of(new ErrorDetail(
                                context.fieldName(),
                                ErrorCategory.INVALID_VALUE,
                                "phải là số dương"
                        ));
                    }
                    return Optional.empty();
                }
        );
    }

    /**
     * couponId: không được null và phải > 0
     */
    public List<ValidationRule<Long>> couponIdRules() {
        return List.of(
                new ValidationRule<Long>() {
                    @Override
                    public Optional<ErrorDetail> validate(com.cinemaebooking.backend.common.validation.engine.ValidationContext<Long> context) {
                        Long value = context.value();
                        if (value == null) {
                            return Optional.of(new ErrorDetail(
                                    context.fieldName(),
                                    ErrorCategory.REQUIRED,
                                    "không được để trống"
                            ));
                        }
                        if (value <= 0) {
                            return Optional.of(new ErrorDetail(
                                    context.fieldName(),
                                    ErrorCategory.INVALID_VALUE,
                                    "phải là số dương"
                            ));
                        }
                        return Optional.empty();
                    }
                }
        );
    }

    /**
     * userCouponId: không được null và phải > 0
     */
    public List<ValidationRule<Long>> userCouponIdRules() {
        return List.of(
                new ValidationRule<Long>() {
                    @Override
                    public Optional<ErrorDetail> validate(com.cinemaebooking.backend.common.validation.engine.ValidationContext<Long> context) {
                        Long value = context.value();
                        if (value == null) {
                            return Optional.of(new ErrorDetail(
                                    context.fieldName(),
                                    ErrorCategory.REQUIRED,
                                    "không được để trống"
                            ));
                        }
                        if (value <= 0) {
                            return Optional.of(new ErrorDetail(
                                    context.fieldName(),
                                    ErrorCategory.INVALID_VALUE,
                                    "phải là số dương"
                            ));
                        }
                        return Optional.empty();
                    }
                }
        );
    }
}