package com.cinemaebooking.backend.coupon.infrastructure.persistence.entity;

import com.cinemaebooking.backend.coupon.domain.enums.CouponType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("CouponJpaEntity Unit Tests - Pure Persistence Model")
class CouponJpaEntityTest {

    // ===================== CREATION TESTS =====================

    @Nested
    @DisplayName("Object Creation")
    class CreationTests {

        @Test
        @DisplayName("Should create CouponJpaEntity successfully using builder")
        void shouldCreateCouponJpaEntityWithBuilder() {
            CouponJpaEntity coupon = CouponJpaEntity.builder()
                    .code("SUMMER2026")
                    .type(CouponType.PERCENT)
                    .value(BigDecimal.valueOf(15.0))
                    .usageLimit(100)
                    .perUserUsage(1)
                    .pointsToRedeem(0)
                    .minimumBookingValue(BigDecimal.valueOf(200000))
                    .maximumDiscountAmount(BigDecimal.valueOf(500000))
                    .startDate(LocalDate.of(2026, 6, 1))
                    .endDate(LocalDate.of(2026, 8, 31))
                    .build();

            assertThat(coupon).isNotNull();
            assertThat(coupon.getCode()).isEqualTo("SUMMER2026");
            assertThat(coupon.getType()).isEqualTo(CouponType.PERCENT);
            assertThat(coupon.getValue()).isEqualTo(BigDecimal.valueOf(15.0));
            assertThat(coupon.getUsageLimit()).isEqualTo(100);
            assertThat(coupon.getPerUserUsage()).isEqualTo(1);
            assertThat(coupon.getPointsToRedeem()).isEqualTo(0);
            assertThat(coupon.getMinimumBookingValue()).isEqualTo(BigDecimal.valueOf(200000));
            assertThat(coupon.getMaximumDiscountAmount()).isEqualTo(BigDecimal.valueOf(500000));
            assertThat(coupon.getStartDate()).isEqualTo(LocalDate.of(2026, 6, 1));
            assertThat(coupon.getEndDate()).isEqualTo(LocalDate.of(2026, 8, 31));
        }

        @Test
        @DisplayName("Should support toBuilder() for partial updates")
        void shouldSupportToBuilder() {
            CouponJpaEntity original = CouponJpaEntity.builder()
                    .code("WINTER2026")
                    .type(CouponType.FIXED)
                    .value(BigDecimal.valueOf(50000))
                    .usageLimit(50)
                    .perUserUsage(1)
                    .pointsToRedeem(100)
                    .startDate(LocalDate.of(2026, 12, 1))
                    .endDate(LocalDate.of(2026, 12, 31))
                    .build();

            CouponJpaEntity updated = original.toBuilder()
                    .value(BigDecimal.valueOf(75000))
                    .usageLimit(30)
                    .endDate(LocalDate.of(2027, 1, 15))
                    .build();

            assertThat(updated.getCode()).isEqualTo("WINTER2026");
            assertThat(updated.getType()).isEqualTo(CouponType.FIXED);
            assertThat(updated.getValue()).isEqualTo(BigDecimal.valueOf(75000));
            assertThat(updated.getUsageLimit()).isEqualTo(30);
            assertThat(updated.getEndDate()).isEqualTo(LocalDate.of(2027, 1, 15));

            // Unchanged fields
            assertThat(updated.getPerUserUsage()).isEqualTo(1);
            assertThat(updated.getPointsToRedeem()).isEqualTo(100);
        }
    }

    // ===================== FIELD MAPPING TESTS =====================

    @Nested
    @DisplayName("Field Mapping")
    class FieldMappingTests {

        @Test
        @DisplayName("Should correctly map all fields")
        void shouldCorrectlyMapAllFields() {
            CouponJpaEntity coupon = CouponJpaEntity.builder()
                    .code("SPRING2026")
                    .type(CouponType.PERCENT)
                    .value(BigDecimal.valueOf(25.5))
                    .usageLimit(200)
                    .perUserUsage(2)
                    .pointsToRedeem(50)
                    .minimumBookingValue(BigDecimal.valueOf(150000))
                    .maximumDiscountAmount(BigDecimal.valueOf(300000))
                    .startDate(LocalDate.now())
                    .endDate(LocalDate.now().plusMonths(4))
                    .build();

            assertThat(coupon.getCode()).isEqualTo("SPRING2026");
            assertThat(coupon.getType()).isEqualTo(CouponType.PERCENT);
            assertThat(coupon.getValue()).isEqualByComparingTo(BigDecimal.valueOf(25.5));
            assertThat(coupon.getUsageLimit()).isEqualTo(200);
            assertThat(coupon.getPerUserUsage()).isEqualTo(2);
            assertThat(coupon.getPointsToRedeem()).isEqualTo(50);
            assertThat(coupon.getMinimumBookingValue()).isEqualByComparingTo(BigDecimal.valueOf(150000));
            assertThat(coupon.getMaximumDiscountAmount()).isEqualByComparingTo(BigDecimal.valueOf(300000));
        }
    }

    // ===================== ENUM MAPPING TESTS =====================

    @Nested
    @DisplayName("Enum Mapping")
    class EnumMappingTests {

        @Test
        @DisplayName("Should map PERCENT enum correctly")
        void shouldMapPercentEnumCorrectly() {
            CouponJpaEntity coupon = CouponJpaEntity.builder()
                    .code("PERCENT15")
                    .type(CouponType.PERCENT)
                    .value(BigDecimal.valueOf(15.0))
                    .usageLimit(100)
                    .perUserUsage(1)
                    .pointsToRedeem(0)
                    .startDate(LocalDate.now())
                    .endDate(LocalDate.now().plusMonths(3))
                    .build();

            assertThat(coupon.getType()).isEqualTo(CouponType.PERCENT);
        }

        @Test
        @DisplayName("Should map FIXED enum correctly")
        void shouldMapFixedEnumCorrectly() {
            CouponJpaEntity coupon = CouponJpaEntity.builder()
                    .code("FIXED100K")
                    .type(CouponType.FIXED)
                    .value(BigDecimal.valueOf(100000))
                    .usageLimit(50)
                    .perUserUsage(1)
                    .pointsToRedeem(120)
                    .startDate(LocalDate.now())
                    .endDate(LocalDate.now().plusMonths(6))
                    .build();

            assertThat(coupon.getType()).isEqualTo(CouponType.FIXED);
        }
    }

    // ===================== EDGE CASES =====================

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCases {

        @Test
        @DisplayName("Should allow zero and minimum boundary values")
        void shouldAllowBoundaryValues() {
            CouponJpaEntity coupon = CouponJpaEntity.builder()
                    .code("MINCOUPON")
                    .type(CouponType.FIXED)
                    .value(BigDecimal.ZERO)
                    .usageLimit(0)
                    .perUserUsage(0)
                    .pointsToRedeem(0)
                    .startDate(LocalDate.now())
                    .endDate(LocalDate.now().plusDays(1))
                    .build();

            assertThat(coupon.getUsageLimit()).isZero();
            assertThat(coupon.getPerUserUsage()).isZero();
            assertThat(coupon.getValue()).isEqualByComparingTo(BigDecimal.ZERO);
        }

        @Test
        @DisplayName("Should allow null for optional fields")
        void shouldAllowNullForOptionalFields() {
            CouponJpaEntity coupon = CouponJpaEntity.builder()
                    .code("OPTIONAL")
                    .type(CouponType.PERCENT)
                    .value(BigDecimal.valueOf(10.0))
                    .usageLimit(100)
                    .perUserUsage(1)
                    .pointsToRedeem(0)
                    .startDate(LocalDate.now())
                    .endDate(LocalDate.now().plusMonths(1))
                    .build();

            assertThat(coupon.getMinimumBookingValue()).isNull();
            assertThat(coupon.getMaximumDiscountAmount()).isNull();
        }
    }
}