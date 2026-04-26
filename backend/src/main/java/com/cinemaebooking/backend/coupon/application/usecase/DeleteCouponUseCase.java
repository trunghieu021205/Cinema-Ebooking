package com.cinemaebooking.backend.coupon.application.usecase;

import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.common.exception.domain.CouponExceptions;
import com.cinemaebooking.backend.coupon.application.port.CouponRepository;
import com.cinemaebooking.backend.coupon.domain.valueobject.CouponId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteCouponUseCase {

    private final CouponRepository couponRepository;

    public void execute(CouponId id) {
        if (id == null) {
            throw CommonExceptions.invalidInput("Coupon id must not be null");
        }
        if (!couponRepository.existsById(id)) {
            throw CouponExceptions.notFound(id);
        }
        couponRepository.deleteById(id);
    }
}