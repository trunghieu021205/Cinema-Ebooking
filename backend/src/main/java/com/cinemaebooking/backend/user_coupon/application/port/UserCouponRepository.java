package com.cinemaebooking.backend.user_coupon.application.port;

import com.cinemaebooking.backend.user_coupon.domain.model.UserCoupon;
import com.cinemaebooking.backend.user_coupon.domain.valueobject.UserCouponId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserCouponRepository {
    UserCoupon create(UserCoupon userCoupon);
    UserCoupon update(UserCoupon userCoupon);
    Optional<UserCoupon> findById(UserCouponId id);
    Page<UserCoupon> findByUserId(Long userId, Pageable pageable);
    boolean existsByUserIdAndCouponId(Long userId, Long couponId);
}