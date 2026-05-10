package com.cinemaebooking.backend.booking_coupon.infrastructure.mapper;

import com.cinemaebooking.backend.booking_coupon.domain.model.BookingCoupon;
import com.cinemaebooking.backend.booking_coupon.domain.valueObject.BookingCouponId;
import com.cinemaebooking.backend.booking_coupon.infrastructure.persistence.entity.BookingCouponJpaEntity;
import com.cinemaebooking.backend.user_coupon.infrastructure.persistence.entity.UserCouponJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class BookingCouponMapperImpl implements BookingCouponMapper {

    @Override
    public BookingCouponJpaEntity toEntity(BookingCoupon domain) {
        return BookingCouponJpaEntity.builder()
                .id(domain.getId() != null ? domain.getId().getValue() : null)
                .userCoupon(UserCouponJpaEntity.builder().id(domain.getUserCouponId()).build())
                .couponCode(domain.getCode())
                .discountAmount(domain.getDiscountValue())
                .appliedAt(domain.getAppliedAt())
                .build();
    }

    @Override
    public BookingCoupon toDomain(BookingCouponJpaEntity entity) {
        return BookingCoupon.builder()
                .id(BookingCouponId.of(entity.getId()))
                .bookingId(entity.getBooking().getId())
                .userCouponId(entity.getUserCoupon().getId())
                .code(entity.getCouponCode())
                .discountValue(entity.getDiscountAmount())
                .appliedAt(entity.getAppliedAt())
                .build();
    }
}
