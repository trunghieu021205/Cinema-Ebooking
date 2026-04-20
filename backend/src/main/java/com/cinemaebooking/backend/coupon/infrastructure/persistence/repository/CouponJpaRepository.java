package com.cinemaebooking.backend.coupon.infrastructure.persistence.repository;

import com.cinemaebooking.backend.coupon.infrastructure.persistence.entity.CouponJpaEntity;
import com.cinemaebooking.backend.infrastructure.persistence.repository.SoftDeleteJpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

/**
 * CouponJpaRepository - JPA repository for Coupon entity.
 * Responsibility:
 * - Provide data access methods for CouponJpaEntity
 * - Support validation and query operations
 *
 * @author Hieu Nguyen
 * @since 2026
 */
@Repository
public interface CouponJpaRepository extends SoftDeleteJpaRepository<CouponJpaEntity> {

    boolean existsByCode(String code);

    boolean existsByCodeAndIdNot(String code, Long id);

    Optional<CouponJpaEntity> findByCodeIgnoreCase(String code);

    boolean existsByCodeAndEndDateAfter(String code, LocalDate currentDate);

    boolean existsByCodeAndUsageLimitGreaterThan(String code, Integer usedCount);

    boolean existsByCodeAndEndDateAfterAndUsageLimitGreaterThan(
            String code, LocalDate currentDate, Integer usedCount);
}