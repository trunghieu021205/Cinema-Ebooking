package com.cinemaebooking.backend.user_coupon.infrastructure.adapter;

import com.cinemaebooking.backend.coupon.application.port.CouponRepository;
import com.cinemaebooking.backend.coupon.domain.model.Coupon;
import com.cinemaebooking.backend.coupon.domain.valueobject.CouponId;
import com.cinemaebooking.backend.user_coupon.application.port.CouponPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CouponAdapter implements CouponPort {

    private final CouponRepository couponRepository;

    @Override
    public Optional<CouponSnapshot> findValidCoupon(Long couponId, LocalDateTime now) {
        CouponId id = CouponId.of(couponId);
        return couponRepository.findById(id)
                .filter(coupon -> coupon.isWithinValidityPeriod())
                .map(coupon -> new CouponSnapshot(
                        coupon.getId().getValue(),
                        true,
                        toDateTime(coupon.getEndDate()),
                        coupon.getPointsToRedeem(),
                        coupon.getPerUserUsage()
                ));
    }

    private LocalDateTime toDateTime(LocalDate date) {
        return date == null ? null : LocalDateTime.of(date, LocalTime.MAX);
    }
}