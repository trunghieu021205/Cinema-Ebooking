package com.cinemaebooking.backend.coupon.application.usecase;

import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.common.exception.domain.CouponExceptions;
import com.cinemaebooking.backend.coupon.application.dto.CouponResponse;
import com.cinemaebooking.backend.coupon.application.mapper.CouponResponseMapper;
import com.cinemaebooking.backend.coupon.application.port.CouponRepository;
import com.cinemaebooking.backend.coupon.domain.valueobject.CouponId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetCouponDetailUseCase {

    private final CouponRepository couponRepository;
    private final CouponResponseMapper mapper;

    public CouponResponse execute(CouponId id) {
        if (id == null) {
            throw CommonExceptions.invalidInput("Coupon id must not be null");
        }
        return couponRepository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> CouponExceptions.notFound(id));
    }
}