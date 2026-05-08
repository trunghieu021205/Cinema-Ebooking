package com.cinemaebooking.backend.user_coupon.application.usecase;

import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.common.exception.domain.UserCouponExceptions;
import com.cinemaebooking.backend.user_coupon.application.dto.UserCouponResponse;
import com.cinemaebooking.backend.user_coupon.application.mapper.UserCouponResponseMapper;
import com.cinemaebooking.backend.user_coupon.application.port.UserCouponRepository;
import com.cinemaebooking.backend.user_coupon.domain.valueobject.UserCouponId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetUserCouponDetailUseCase {

    private final UserCouponRepository userCouponRepository;
    private final UserCouponResponseMapper mapper;

    public UserCouponResponse execute(UserCouponId id) {
        if (id == null) {
            throw CommonExceptions.invalidInput("UserCoupon id must not be null");
        }
        return userCouponRepository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> UserCouponExceptions.notFound(id));
    }
}