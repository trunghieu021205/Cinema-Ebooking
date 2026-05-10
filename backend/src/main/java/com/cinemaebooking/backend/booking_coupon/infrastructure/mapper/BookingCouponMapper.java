package com.cinemaebooking.backend.booking_coupon.infrastructure.mapper;

import com.cinemaebooking.backend.booking_coupon.domain.model.BookingCoupon;
import com.cinemaebooking.backend.booking_coupon.infrastructure.persistence.entity.BookingCouponJpaEntity;
import com.cinemaebooking.backend.infrastructure.mapper.BaseMapper;

public interface BookingCouponMapper extends BaseMapper<BookingCoupon, BookingCouponJpaEntity> {
}
