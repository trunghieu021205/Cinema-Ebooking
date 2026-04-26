package com.cinemaebooking.backend.coupon.domain.model;

import com.cinemaebooking.backend.common.domain.BaseEntity;
import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.coupon.domain.enums.CouponType;
import com.cinemaebooking.backend.coupon.domain.valueobject.CouponId;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@SuperBuilder
public class Coupon extends BaseEntity<CouponId> {

    private String code;
    private CouponType type;
    private BigDecimal value;
    private Integer usageLimit;
    private Integer perUserUsage;
    private Integer pointsToRedeem;
    private BigDecimal minimumBookingValue;
    private BigDecimal maximumDiscountAmount;
    private LocalDate startDate;
    private LocalDate endDate;

    // ================== BUSINESS METHODS ==================

    public void update(String code,
                       CouponType type,
                       BigDecimal value,
                       Integer usageLimit,
                       Integer perUserUsage,
                       Integer pointsToRedeem,
                       BigDecimal minimumBookingValue,
                       BigDecimal maximumDiscountAmount,
                       LocalDate startDate,
                       LocalDate endDate) {
        validateUpdateData(code, type, value, usageLimit, perUserUsage,
                pointsToRedeem, minimumBookingValue, maximumDiscountAmount,
                startDate, endDate);

        this.code = code;
        this.type = type;
        this.value = value;
        this.usageLimit = usageLimit;
        this.perUserUsage = perUserUsage;
        this.pointsToRedeem = pointsToRedeem;
        this.minimumBookingValue = minimumBookingValue;
        this.maximumDiscountAmount = maximumDiscountAmount;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public boolean isActive() {
        LocalDate now = LocalDate.now();
        return !startDate.isAfter(now) && !endDate.isBefore(now);
    }

    public boolean isExpired() {
        return endDate.isBefore(LocalDate.now());
    }

    public boolean isWithinValidityPeriod() {
        return !startDate.isAfter(LocalDate.now()) && !endDate.isBefore(LocalDate.now());
    }

    // ================== VALIDATION ==================

    private void validateUpdateData(String code,
                                    CouponType type,
                                    BigDecimal value,
                                    Integer usageLimit,
                                    Integer perUserUsage,
                                    Integer pointsToRedeem,
                                    BigDecimal minimumBookingValue,
                                    BigDecimal maximumDiscountAmount,
                                    LocalDate startDate,
                                    LocalDate endDate) {
        validateCode(code);
        validateType(type);
        validateValue(value, type);
        validateUsageLimit(usageLimit);
        validatePerUserUsage(perUserUsage);
        validatePointsToRedeem(pointsToRedeem);
        validateMinimumBookingValue(minimumBookingValue);
        validateMaximumDiscountAmount(maximumDiscountAmount, type);
        validateDateRange(startDate, endDate);

        // Cross-field validations
        if (type == CouponType.PERCENT && value.compareTo(new BigDecimal("100")) > 0) {
            throw CommonExceptions.invalidInput("Percent discount value cannot exceed 100");
        }
        if (type == CouponType.PERCENT && maximumDiscountAmount != null &&
                minimumBookingValue != null && maximumDiscountAmount.compareTo(minimumBookingValue) > 0) {
            throw CommonExceptions.invalidInput("Maximum discount amount cannot exceed minimum booking value");
        }
    }

    private void validateCode(String code) {
        if (code == null || code.isBlank()) {
            throw CommonExceptions.invalidInput("Coupon code must not be blank");
        }
        if (code.length() < 3 || code.length() > 100) {
            throw CommonExceptions.invalidInput("Coupon code length must be between 3 and 100 characters");
        }
        if (!code.matches("^[A-Z0-9_-]+$")) {
            throw CommonExceptions.invalidInput("Coupon code must contain only uppercase letters, numbers, underscore or hyphen");
        }
    }

    private void validateType(CouponType type) {
        if (type == null) {
            throw CommonExceptions.invalidInput("Coupon type must not be null");
        }
    }

    private void validateValue(BigDecimal value, CouponType type) {
        if (value == null) {
            throw CommonExceptions.invalidInput("Discount value must not be null");
        }
        if (value.compareTo(BigDecimal.ZERO) <= 0) {
            throw CommonExceptions.invalidInput("Discount value must be positive");
        }
    }

    private void validateUsageLimit(Integer usageLimit) {
        if (usageLimit == null || usageLimit < 1) {
            throw CommonExceptions.invalidInput("Usage limit must be at least 1");
        }
    }

    private void validatePerUserUsage(Integer perUserUsage) {
        if (perUserUsage == null || perUserUsage < 1) {
            throw CommonExceptions.invalidInput("Per-user usage must be at least 1");
        }
    }

    private void validatePointsToRedeem(Integer pointsToRedeem) {
        if (pointsToRedeem == null || pointsToRedeem < 0) {
            throw CommonExceptions.invalidInput("Points to redeem must be non-negative");
        }
    }

    private void validateMinimumBookingValue(BigDecimal minimumBookingValue) {
        if (minimumBookingValue != null && minimumBookingValue.compareTo(BigDecimal.ZERO) < 0) {
            throw CommonExceptions.invalidInput("Minimum booking value cannot be negative");
        }
    }

    private void validateMaximumDiscountAmount(BigDecimal maximumDiscountAmount, CouponType type) {
        if (type == CouponType.PERCENT) {
            // maximumDiscountAmount là optional nhưng nếu có phải >=0
            if (maximumDiscountAmount != null && maximumDiscountAmount.compareTo(BigDecimal.ZERO) < 0) {
                throw CommonExceptions.invalidInput("Maximum discount amount cannot be negative");
            }
        } else {
            // FIXED type thì không cần maximumDiscountAmount, nhưng nếu có thì báo lỗi
            if (maximumDiscountAmount != null) {
                throw CommonExceptions.invalidInput("Maximum discount amount is not applicable for FIXED coupons");
            }
        }
    }

    private void validateDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw CommonExceptions.invalidInput("Start date and end date must not be null");
        }
        if (endDate.isBefore(startDate)) {
            throw CommonExceptions.invalidInput("End date must be after start date");
        }
    }
    public void validateAll() {
        validateUpdateData(this.code, this.type, this.value, this.usageLimit,
                this.perUserUsage, this.pointsToRedeem, this.minimumBookingValue,
                this.maximumDiscountAmount, this.startDate, this.endDate);
    }
}