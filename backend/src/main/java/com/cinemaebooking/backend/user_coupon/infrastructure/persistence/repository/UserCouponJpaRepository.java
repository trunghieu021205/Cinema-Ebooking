package com.cinemaebooking.backend.user_coupon.infrastructure.persistence.repository;


import com.cinemaebooking.backend.infrastructure.persistence.repository.SoftDeleteJpaRepository;
import com.cinemaebooking.backend.user_coupon.domain.enums.UserCouponStatus;
import com.cinemaebooking.backend.user_coupon.infrastructure.persistence.entity.UserCouponJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * UserCouponJpaRepository - JPA repository for UserCoupon entity.
 * Responsibility:
 * - Provide database access methods for UserCoupon
 * - Support validation queries and business queries
 *
 * @author Hieu Nguyen
 * @since 2026
 */
@Repository
public interface UserCouponJpaRepository extends SoftDeleteJpaRepository<UserCouponJpaEntity> {

    boolean existsByUserIdAndCouponId(Long userId, Long couponId);

    boolean existsByUserIdAndCouponIdAndIdNot(Long userId, Long couponId, Long id);

    Optional<UserCouponJpaEntity> findByUserIdAndCouponId(Long userId, Long couponId);

    Page<UserCouponJpaEntity> findByUserId(Long userId, Pageable pageable);

    Page<UserCouponJpaEntity> findByUserIdAndStatus(Long userId, UserCouponStatus status, Pageable pageable);

    // Returns only coupons that have not yet expired at the given timestamp
    Page<UserCouponJpaEntity> findByUserIdAndExpiredAtAfter(Long userId, LocalDateTime now, Pageable pageable);

    Page<UserCouponJpaEntity> findByUserIdAndUsageRemainGreaterThan(Long userId, Integer usageRemain, Pageable pageable);
}