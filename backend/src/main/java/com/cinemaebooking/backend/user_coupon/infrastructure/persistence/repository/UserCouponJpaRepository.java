package com.cinemaebooking.backend.user_coupon.infrastructure.persistence.repository;


import com.cinemaebooking.backend.infrastructure.persistence.repository.SoftDeleteJpaRepository;
import com.cinemaebooking.backend.user_coupon.domain.enums.UserCouponStatus;
import com.cinemaebooking.backend.user_coupon.infrastructure.persistence.entity.UserCouponJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
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

    List<UserCouponJpaEntity> findByStatusAndExpiredAtBefore(UserCouponStatus status, LocalDateTime dateTime);

    @Query("""
    SELECT uc FROM UserCouponJpaEntity uc 
    JOIN FETCH CouponJpaEntity c ON uc.couponId = c.id 
    WHERE uc.userId = :userId AND c.code = :code
""")
    Optional<UserCouponJpaEntity> findByUserIdAndCodeWithCoupon(@Param("userId") Long userId, @Param("code") String code);

    @Modifying
    @Transactional // Quan trọng: Phải có để thực thi lệnh update
    @Query("UPDATE UserCouponJpaEntity uc SET uc.status = :status WHERE uc.id = :id")
    void updateStatus(@Param("id") Long id, @Param("status") UserCouponStatus status);
}