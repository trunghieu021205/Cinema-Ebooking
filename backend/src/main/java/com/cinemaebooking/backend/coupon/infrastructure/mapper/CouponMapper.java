package com.cinemaebooking.backend.coupon.infrastructure.mapper;

import com.cinemaebooking.backend.coupon.domain.model.Coupon;
import com.cinemaebooking.backend.coupon.infrastructure.persistence.entity.CouponJpaEntity;
import com.cinemaebooking.backend.infrastructure.mapper.BaseMapper;

public interface CouponMapper extends BaseMapper<Coupon, CouponJpaEntity> {

    void updateEntity(CouponJpaEntity entity, Coupon domain);
}