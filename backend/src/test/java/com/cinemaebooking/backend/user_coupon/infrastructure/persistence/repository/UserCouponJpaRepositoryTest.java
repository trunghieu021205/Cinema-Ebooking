package com.cinemaebooking.backend.user_coupon.infrastructure.persistence.repository;

import com.cinemaebooking.backend.user_coupon.domain.enums.UserCouponStatus;
import com.cinemaebooking.backend.user_coupon.infrastructure.persistence.entity.UserCouponJpaEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * UserCouponJpaRepositoryTest - Integration test for UserCouponJpaRepository.
 * Responsibility:
 * - Verify query methods behavior with real database
 * - Ensure filtering, validation, and pagination work correctly
 *
 * @author Hieu Nguyen
 * @since 2026
 */
@DataJpaTest
@DisplayName("UserCouponJpaRepository")
class UserCouponJpaRepositoryTest {

    private static final Long USER_ID_1    = 1L;
    private static final Long USER_ID_2    = 2L;
    private static final Long COUPON_ID_A  = 10L;
    private static final Long COUPON_ID_B  = 20L;
    private static final Long COUPON_ID_C  = 30L;
    private static final int  PAGE_SIZE    = 2;
    private static final int  PAGE_FIRST   = 0;
    private static final int  PAGE_SECOND  = 1;
    private static final int  USAGE_NONE   = 0;
    private static final int  USAGE_ONE    = 1;
    private static final int  USAGE_THREE  = 3;

    @Autowired private TestEntityManager em;
    @Autowired private UserCouponJpaRepository repository;

    @Nested
    @DisplayName("existsByUserIdAndCouponId")
    class ExistsByUserIdAndCouponId {

        @Test
        @DisplayName("should return true when record exists for the given user and coupon")
        void should_return_true_when_pair_exists() {
            persist(builder(USER_ID_1, COUPON_ID_A).build());

            boolean result = repository.existsByUserIdAndCouponId(USER_ID_1, COUPON_ID_A);

            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("should return false when no record exists for the given user and coupon")
        void should_return_false_when_pair_does_not_exist() {
            boolean result = repository.existsByUserIdAndCouponId(USER_ID_1, COUPON_ID_A);

            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("should return false when the same coupon belongs to a different user")
        void should_return_false_for_different_user() {
            persist(builder(USER_ID_2, COUPON_ID_A).build());

            boolean result = repository.existsByUserIdAndCouponId(USER_ID_1, COUPON_ID_A);

            assertThat(result).isFalse();
        }
    }

    @Nested
    @DisplayName("existsByUserIdAndCouponIdAndIdNot")
    class ExistsByUserIdAndCouponIdAndIdNot {

        @Test
        @DisplayName("should return false when the only matching record is the one being excluded")
        void should_return_false_when_only_self_matches() {
            UserCouponJpaEntity entity = persist(builder(USER_ID_1, COUPON_ID_A).build());

            boolean result = repository.existsByUserIdAndCouponIdAndIdNot(
                    USER_ID_1, COUPON_ID_A, entity.getId());

            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("should return true when another record with the same user-coupon pair exists")
        void should_return_true_when_another_duplicate_exists() {
            persist(builder(USER_ID_1, COUPON_ID_A).build());
            UserCouponJpaEntity self = persist(builder(USER_ID_1, COUPON_ID_A).build());

            boolean result = repository.existsByUserIdAndCouponIdAndIdNot(
                    USER_ID_1, COUPON_ID_A, self.getId());

            assertThat(result).isTrue();
        }
    }

    @Nested
    @DisplayName("findByUserIdAndCouponId")
    class FindByUserIdAndCouponId {

        @Test
        @DisplayName("should return the correct record when user and coupon match")
        void should_return_present_when_pair_exists() {
            persist(builder(USER_ID_1, COUPON_ID_A).build());
            persist(builder(USER_ID_1, COUPON_ID_B).build());
            persist(builder(USER_ID_2, COUPON_ID_A).build());

            Optional<UserCouponJpaEntity> result =
                    repository.findByUserIdAndCouponId(USER_ID_1, COUPON_ID_B);

            assertThat(result).isPresent();
            assertThat(result.get().getUserId()).isEqualTo(USER_ID_1);
            assertThat(result.get().getCouponId()).isEqualTo(COUPON_ID_B);
        }

        @Test
        @DisplayName("should return empty when no record matches the user-coupon pair")
        void should_return_empty_when_pair_does_not_exist() {
            Optional<UserCouponJpaEntity> result =
                    repository.findByUserIdAndCouponId(USER_ID_1, COUPON_ID_C);

            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("findByUserId")
    class FindByUserId {

        @Test
        @DisplayName("should return only records belonging to the queried user")
        void should_return_only_records_of_target_user() {
            persist(builder(USER_ID_1, COUPON_ID_A).build());
            persist(builder(USER_ID_1, COUPON_ID_B).build());
            persist(builder(USER_ID_2, COUPON_ID_C).build());

            Page<UserCouponJpaEntity> page =
                    repository.findByUserId(USER_ID_1, PageRequest.of(PAGE_FIRST, PAGE_SIZE));

            assertThat(page.getContent()).hasSize(2);
            assertThat(page.getContent()).allMatch(e -> e.getUserId().equals(USER_ID_1));
        }

        @Test
        @DisplayName("should not return records belonging to a different user")
        void should_not_mix_records_between_users() {
            persist(builder(USER_ID_1, COUPON_ID_A).build());
            persist(builder(USER_ID_2, COUPON_ID_A).build());

            Page<UserCouponJpaEntity> user1Page =
                    repository.findByUserId(USER_ID_1, PageRequest.of(PAGE_FIRST, PAGE_SIZE));
            Page<UserCouponJpaEntity> user2Page =
                    repository.findByUserId(USER_ID_2, PageRequest.of(PAGE_FIRST, PAGE_SIZE));

            assertThat(user1Page.getContent()).allMatch(e -> e.getUserId().equals(USER_ID_1));
            assertThat(user2Page.getContent()).allMatch(e -> e.getUserId().equals(USER_ID_2));
        }
    }

    @Nested
    @DisplayName("findByUserIdAndStatus")
    class FindByUserIdAndStatus {

        @Test
        @DisplayName("should return only records with AVAILABLE status")
        void should_filter_by_available_status() {
            persist(builder(USER_ID_1, COUPON_ID_A).status(UserCouponStatus.AVAILABLE).build());
            persist(builder(USER_ID_1, COUPON_ID_B).status(UserCouponStatus.USED).usedAt(LocalDateTime.now()).build());
            persist(builder(USER_ID_1, COUPON_ID_C).status(UserCouponStatus.EXPIRED).build());

            Page<UserCouponJpaEntity> page = repository.findByUserIdAndStatus(
                    USER_ID_1, UserCouponStatus.AVAILABLE, PageRequest.of(PAGE_FIRST, PAGE_SIZE));

            assertThat(page.getContent()).hasSize(1);
            assertThat(page.getContent().get(0).getStatus()).isEqualTo(UserCouponStatus.AVAILABLE);
        }

        @Test
        @DisplayName("should return only records with USED status, each having a non-null usedAt")
        void should_filter_by_used_status_and_verify_usedAt_is_set() {
            LocalDateTime usedAt = LocalDateTime.now().minusDays(1);
            // status=USED must have usedAt set — this verifies data setup consistency
            persist(builder(USER_ID_1, COUPON_ID_A).status(UserCouponStatus.USED).usedAt(usedAt).build());
            persist(builder(USER_ID_1, COUPON_ID_B).status(UserCouponStatus.AVAILABLE).build());

            Page<UserCouponJpaEntity> page = repository.findByUserIdAndStatus(
                    USER_ID_1, UserCouponStatus.USED, PageRequest.of(PAGE_FIRST, PAGE_SIZE));

            assertThat(page.getContent()).hasSize(1);
            assertThat(page.getContent().get(0).getUsedAt()).isNotNull();
        }

        @Test
        @DisplayName("should return only records with EXPIRED status")
        void should_filter_by_expired_status() {
            persist(builder(USER_ID_1, COUPON_ID_A).status(UserCouponStatus.EXPIRED).build());
            persist(builder(USER_ID_1, COUPON_ID_B).status(UserCouponStatus.AVAILABLE).build());

            Page<UserCouponJpaEntity> page = repository.findByUserIdAndStatus(
                    USER_ID_1, UserCouponStatus.EXPIRED, PageRequest.of(PAGE_FIRST, PAGE_SIZE));

            assertThat(page.getContent()).hasSize(1);
            assertThat(page.getContent().get(0).getStatus()).isEqualTo(UserCouponStatus.EXPIRED);
        }
    }

    @Nested
    @DisplayName("findByUserIdAndExpiredAtAfter")
    class FindByUserIdAndExpiredAtAfter {

        @Test
        @DisplayName("should return only coupons whose expiry date is strictly after now")
        void should_return_only_non_expired_coupons() {
            LocalDateTime now = LocalDateTime.now();

            persist(builder(USER_ID_1, COUPON_ID_A).expiredAt(now.plusDays(7)).build());
            persist(builder(USER_ID_1, COUPON_ID_B).expiredAt(now.minusDays(1)).build());

            Page<UserCouponJpaEntity> page = repository.findByUserIdAndExpiredAtAfter(
                    USER_ID_1, now, PageRequest.of(PAGE_FIRST, PAGE_SIZE));

            assertThat(page.getContent()).hasSize(1);
            assertThat(page.getContent().get(0).getCouponId()).isEqualTo(COUPON_ID_A);
        }

        @Test
        @DisplayName("should return empty when all coupons are already expired")
        void should_return_empty_when_all_expired() {
            LocalDateTime now = LocalDateTime.now();

            persist(builder(USER_ID_1, COUPON_ID_A).expiredAt(now.minusDays(3)).build());
            persist(builder(USER_ID_1, COUPON_ID_B).expiredAt(now.minusDays(1)).build());

            Page<UserCouponJpaEntity> page = repository.findByUserIdAndExpiredAtAfter(
                    USER_ID_1, now, PageRequest.of(PAGE_FIRST, PAGE_SIZE));

            assertThat(page.getContent()).isEmpty();
        }
    }

    @Nested
    @DisplayName("findByUserIdAndUsageRemainGreaterThan")
    class FindByUserIdAndUsageRemainGreaterThan {

        @Test
        @DisplayName("should return only coupons with usageRemain above the threshold")
        void should_return_coupons_with_positive_usage_remain() {
            persist(builder(USER_ID_1, COUPON_ID_A).usageRemain(USAGE_NONE).build());
            persist(builder(USER_ID_1, COUPON_ID_B).usageRemain(USAGE_ONE).build());
            persist(builder(USER_ID_1, COUPON_ID_C).usageRemain(USAGE_THREE).build());

            Page<UserCouponJpaEntity> page = repository.findByUserIdAndUsageRemainGreaterThan(
                    USER_ID_1, USAGE_NONE, PageRequest.of(PAGE_FIRST, PAGE_SIZE));

            assertThat(page.getTotalElements()).isEqualTo(2);
            assertThat(page.getContent()).allMatch(e -> e.getUsageRemain() > USAGE_NONE);
        }

        @Test
        @DisplayName("should not return coupon with usageRemain = 0")
        void should_exclude_zero_usage_remain() {
            // usageRemain = 0 means the coupon is exhausted — must not appear in usable query
            persist(builder(USER_ID_1, COUPON_ID_A).usageRemain(USAGE_NONE).build());

            Page<UserCouponJpaEntity> page = repository.findByUserIdAndUsageRemainGreaterThan(
                    USER_ID_1, USAGE_NONE, PageRequest.of(PAGE_FIRST, PAGE_SIZE));

            assertThat(page.getContent()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Pagination")
    class Pagination {

        @Test
        @DisplayName("should return correct metadata and content size for page 0")
        void should_paginate_page_zero() {
            persist(builder(USER_ID_1, COUPON_ID_A).build());
            persist(builder(USER_ID_1, COUPON_ID_B).build());
            persist(builder(USER_ID_1, COUPON_ID_C).build());

            Page<UserCouponJpaEntity> page =
                    repository.findByUserId(USER_ID_1, PageRequest.of(PAGE_FIRST, PAGE_SIZE));

            assertThat(page.getTotalElements()).isEqualTo(3);
            assertThat(page.getTotalPages()).isEqualTo(2);
            assertThat(page.getContent()).hasSize(PAGE_SIZE);
        }

        @Test
        @DisplayName("should return the remaining record on page 1")
        void should_paginate_page_one() {
            persist(builder(USER_ID_1, COUPON_ID_A).build());
            persist(builder(USER_ID_1, COUPON_ID_B).build());
            persist(builder(USER_ID_1, COUPON_ID_C).build());

            Page<UserCouponJpaEntity> page =
                    repository.findByUserId(USER_ID_1, PageRequest.of(PAGE_SECOND, PAGE_SIZE));

            assertThat(page.getTotalElements()).isEqualTo(3);
            assertThat(page.getTotalPages()).isEqualTo(2);
            assertThat(page.getContent()).hasSize(1);
        }
    }

    private UserCouponJpaEntity persist(UserCouponJpaEntity entity) {
        UserCouponJpaEntity saved = em.persist(entity);
        em.flush();
        return saved;
    }

    private EntityBuilder builder(Long userId, Long couponId) {
        return new EntityBuilder(userId, couponId);
    }

    /**
     * Fluent builder for UserCouponJpaEntity test fixtures.
     * Provides sensible defaults so each test only declares what it cares about.
     */
    private static class EntityBuilder {
        private final Long userId;
        private final Long couponId;
        private UserCouponStatus status     = UserCouponStatus.AVAILABLE;
        private LocalDateTime    receivedAt = LocalDateTime.now();
        private LocalDateTime    expiredAt  = LocalDateTime.now().plusDays(30);
        private LocalDateTime    usedAt     = null;
        private int              usageRemain = USAGE_THREE;

        EntityBuilder(Long userId, Long couponId) {
            this.userId   = userId;
            this.couponId = couponId;
        }

        EntityBuilder status(UserCouponStatus status)      { this.status      = status;      return this; }
        EntityBuilder expiredAt(LocalDateTime expiredAt)   { this.expiredAt   = expiredAt;   return this; }
        EntityBuilder usedAt(LocalDateTime usedAt)         { this.usedAt      = usedAt;      return this; }
        EntityBuilder usageRemain(int usageRemain)         { this.usageRemain = usageRemain; return this; }

        UserCouponJpaEntity build() {
            UserCouponJpaEntity entity = new UserCouponJpaEntity();
            entity.setUserId(userId);
            entity.setCouponId(couponId);
            entity.setStatus(status);
            entity.setReceivedAt(receivedAt);
            entity.setExpiredAt(expiredAt);
            entity.setUsedAt(usedAt);
            entity.setUsageRemain(usageRemain);
            return entity;
        }
    }
}