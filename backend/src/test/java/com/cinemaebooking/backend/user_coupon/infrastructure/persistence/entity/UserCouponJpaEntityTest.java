package com.cinemaebooking.backend.user_coupon.infrastructure.persistence.entity;

import com.cinemaebooking.backend.user_coupon.domain.enums.UserCouponStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("UserCouponJpaEntity Unit Tests - Pure Persistence Model")
class UserCouponJpaEntityTest {

    private static final Long USER_ID = 100L;
    private static final Long COUPON_ID = 200L;
    private static final LocalDateTime FUTURE_TIME = LocalDateTime.now().plusDays(30);
    private static final LocalDateTime PAST_TIME = LocalDateTime.now().minusDays(5);
    private static final LocalDateTime NOW = LocalDateTime.now();

    // ===================== CREATION TESTS =====================

    @Nested
    @DisplayName("Object Creation")
    class CreationTests {

        @Test
        @DisplayName("Should create UserCouponJpaEntity successfully using builder")
        void shouldCreateValidUserCoupon() {
            UserCouponJpaEntity userCoupon = UserCouponJpaEntity.builder()
                    .userId(USER_ID)
                    .couponId(COUPON_ID)
                    .usageRemain(2)
                    .receivedAt(NOW)
                    .expiredAt(FUTURE_TIME)
                    .status(UserCouponStatus.AVAILABLE)
                    .build();

            assertThat(userCoupon).isNotNull();
            assertThat(userCoupon.getUserId()).isEqualTo(USER_ID);
            assertThat(userCoupon.getCouponId()).isEqualTo(COUPON_ID);
            assertThat(userCoupon.getUsageRemain()).isEqualTo(2);
            assertThat(userCoupon.getReceivedAt()).isEqualTo(NOW);
            assertThat(userCoupon.getExpiredAt()).isEqualTo(FUTURE_TIME);
            assertThat(userCoupon.getStatus()).isEqualTo(UserCouponStatus.AVAILABLE);
            assertThat(userCoupon.getUsedAt()).isNull();
        }

        @Test
        @DisplayName("Should allow usedAt when status is USED")
        void shouldCreateUsedCoupon() {
            LocalDateTime usedTime = LocalDateTime.now().minusHours(2);

            UserCouponJpaEntity userCoupon = UserCouponJpaEntity.builder()
                    .userId(USER_ID)
                    .couponId(COUPON_ID)
                    .usageRemain(0)
                    .receivedAt(NOW.minusDays(1))
                    .usedAt(usedTime)
                    .expiredAt(FUTURE_TIME)
                    .status(UserCouponStatus.USED)
                    .build();

            assertThat(userCoupon.getStatus()).isEqualTo(UserCouponStatus.USED);
            assertThat(userCoupon.getUsedAt()).isEqualTo(usedTime);
            assertThat(userCoupon.getReceivedAt()).isNotNull();
        }
    }

    // ===================== FIELD MAPPING TESTS =====================

    @Nested
    @DisplayName("Field Mapping")
    class FieldMappingTests {

        @Test
        @DisplayName("Should correctly map all fields including receivedAt")
        void shouldCorrectlyMapAllFields() {
            LocalDateTime usedTime = LocalDateTime.now().minusHours(1);
            LocalDateTime receivedTime = NOW.minusDays(2);

            UserCouponJpaEntity userCoupon = UserCouponJpaEntity.builder()
                    .userId(USER_ID)
                    .couponId(COUPON_ID)
                    .usageRemain(5)
                    .receivedAt(receivedTime)
                    .usedAt(usedTime)
                    .expiredAt(FUTURE_TIME)
                    .status(UserCouponStatus.AVAILABLE)
                    .build();

            assertThat(userCoupon.getUserId()).isEqualTo(USER_ID);
            assertThat(userCoupon.getCouponId()).isEqualTo(COUPON_ID);
            assertThat(userCoupon.getUsageRemain()).isEqualTo(5);
            assertThat(userCoupon.getReceivedAt()).isEqualTo(receivedTime);
            assertThat(userCoupon.getUsedAt()).isEqualTo(usedTime);
            assertThat(userCoupon.getExpiredAt()).isEqualTo(FUTURE_TIME);
            assertThat(userCoupon.getStatus()).isEqualTo(UserCouponStatus.AVAILABLE);
        }
    }

    // ===================== ENUM MAPPING TESTS =====================

    @Nested
    @DisplayName("Enum Mapping")
    class EnumMappingTests {

        @Test
        @DisplayName("Should map AVAILABLE status correctly")
        void shouldMapAvailableStatus() {
            UserCouponJpaEntity coupon = UserCouponJpaEntity.builder()
                    .userId(USER_ID)
                    .couponId(COUPON_ID)
                    .usageRemain(3)
                    .receivedAt(NOW)
                    .expiredAt(FUTURE_TIME)
                    .status(UserCouponStatus.AVAILABLE)
                    .build();

            assertThat(coupon.getStatus()).isEqualTo(UserCouponStatus.AVAILABLE);
        }

        @Test
        @DisplayName("Should map USED status correctly")
        void shouldMapUsedStatus() {
            UserCouponJpaEntity coupon = UserCouponJpaEntity.builder()
                    .userId(USER_ID)
                    .couponId(COUPON_ID)
                    .usageRemain(0)
                    .receivedAt(NOW.minusDays(1))
                    .usedAt(LocalDateTime.now().minusHours(3))
                    .expiredAt(FUTURE_TIME)
                    .status(UserCouponStatus.USED)
                    .build();

            assertThat(coupon.getStatus()).isEqualTo(UserCouponStatus.USED);
        }

        @Test
        @DisplayName("Should map EXPIRED status correctly")
        void shouldMapExpiredStatus() {
            UserCouponJpaEntity coupon = UserCouponJpaEntity.builder()
                    .userId(USER_ID)
                    .couponId(COUPON_ID)
                    .usageRemain(1)
                    .receivedAt(NOW.minusDays(5))
                    .expiredAt(FUTURE_TIME)
                    .status(UserCouponStatus.EXPIRED)
                    .build();

            assertThat(coupon.getStatus()).isEqualTo(UserCouponStatus.EXPIRED);
        }
    }

    // ===================== EDGE CASES =====================

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCases {

        @Test
        @DisplayName("Should allow usageRemain = 0")
        void shouldAllowZeroUsageRemain() {
            UserCouponJpaEntity coupon = UserCouponJpaEntity.builder()
                    .userId(USER_ID)
                    .couponId(COUPON_ID)
                    .usageRemain(0)
                    .receivedAt(NOW)
                    .expiredAt(FUTURE_TIME)
                    .status(UserCouponStatus.AVAILABLE)
                    .build();

            assertThat(coupon.getUsageRemain()).isZero();
        }

        @Test
        @DisplayName("Should allow null usedAt for AVAILABLE status")
        void shouldAllowNullUsedAtForAvailableStatus() {
            UserCouponJpaEntity coupon = UserCouponJpaEntity.builder()
                    .userId(USER_ID)
                    .couponId(COUPON_ID)
                    .usageRemain(2)
                    .receivedAt(NOW)
                    .expiredAt(FUTURE_TIME)
                    .status(UserCouponStatus.AVAILABLE)
                    .build();

            assertThat(coupon.getUsedAt()).isNull();
        }

        @Test
        @DisplayName("Should allow expiredAt and receivedAt in the past")
        void shouldAllowPastDates() {
            UserCouponJpaEntity coupon = UserCouponJpaEntity.builder()
                    .userId(USER_ID)
                    .couponId(COUPON_ID)
                    .usageRemain(1)
                    .receivedAt(PAST_TIME)
                    .expiredAt(PAST_TIME.plusDays(10))
                    .status(UserCouponStatus.AVAILABLE)
                    .build();

            assertThat(coupon.getReceivedAt()).isEqualTo(PAST_TIME);
            assertThat(coupon.getExpiredAt()).isEqualTo(PAST_TIME.plusDays(10));
        }
    }

    // ===================== BUILDER SUPPORT =====================

    @Nested
    @DisplayName("Builder Support")
    class BuilderSupportTests {

        @Test
        @DisplayName("Should support toBuilder() for partial updates")
        void shouldSupportToBuilder() {
            UserCouponJpaEntity original = UserCouponJpaEntity.builder()
                    .userId(USER_ID)
                    .couponId(COUPON_ID)
                    .usageRemain(5)
                    .receivedAt(NOW)
                    .expiredAt(FUTURE_TIME)
                    .status(UserCouponStatus.AVAILABLE)
                    .build();

            UserCouponJpaEntity updated = original.toBuilder()
                    .usageRemain(3)
                    .status(UserCouponStatus.EXPIRED)
                    .build();

            assertThat(updated.getUserId()).isEqualTo(USER_ID);
            assertThat(updated.getCouponId()).isEqualTo(COUPON_ID);
            assertThat(updated.getUsageRemain()).isEqualTo(3);
            assertThat(updated.getStatus()).isEqualTo(UserCouponStatus.EXPIRED);
            assertThat(updated.getReceivedAt()).isEqualTo(NOW);        // unchanged
            assertThat(updated.getExpiredAt()).isEqualTo(FUTURE_TIME); // unchanged
        }
    }
}