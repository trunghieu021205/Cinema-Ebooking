package com.cinemaebooking.backend.user_coupon.application.port;

import com.cinemaebooking.backend.user_coupon.domain.model.UserCoupon;
import com.cinemaebooking.backend.user_coupon.domain.valueobject.UserCouponId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserCouponRepository {
    UserCoupon create(UserCoupon userCoupon);
    UserCoupon update(UserCoupon userCoupon);
    Optional<UserCoupon> findById(UserCouponId id);
    Optional<UserCoupon> findByUserIdAndCouponId(Long userId, Long couponId);
    Page<UserCoupon> findByUserId(Long userId, Pageable pageable);
    Page<UserCoupon> findByUserIdAndStatus(Long userId, String status, Pageable pageable);
    List<UserCoupon> findAvailableExpiredBefore(LocalDateTime dateTime);
    boolean existsByUserIdAndCouponId(Long userId, Long couponId);
    boolean existsById(UserCouponId id);
    void deleteById(UserCouponId id);
}