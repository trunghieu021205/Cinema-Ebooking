package com.cinemaebooking.backend.user_coupon.infrastructure.mapper;

import com.cinemaebooking.backend.infrastructure.mapper.BaseMapper;
import com.cinemaebooking.backend.user_coupon.domain.model.UserCoupon;
import com.cinemaebooking.backend.user_coupon.infrastructure.persistence.entity.UserCouponJpaEntity;

public interface UserCouponMapper extends BaseMapper<UserCoupon, UserCouponJpaEntity> {
    void updateEntity(UserCouponJpaEntity entity, UserCoupon domain);
}