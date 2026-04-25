package com.cinemaebooking.backend.coupon.application.usecase;

import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.coupon.application.dto.CouponResponse;
import com.cinemaebooking.backend.coupon.application.mapper.CouponResponseMapper;
import com.cinemaebooking.backend.coupon.application.port.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetCouponListUseCase {

    private final CouponRepository couponRepository;
    private final CouponResponseMapper mapper;

    public Page<CouponResponse> execute(Pageable pageable) {
        if (pageable == null) {
            throw CommonExceptions.invalidInput("Pageable must not be null");
        }
        return couponRepository.findAll(pageable)
                .map(mapper::toResponse);
    }
}