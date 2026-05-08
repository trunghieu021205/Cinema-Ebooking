package com.cinemaebooking.backend.user_coupon.application.usecase;

import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.user_coupon.application.dto.UserCouponResponse;
import com.cinemaebooking.backend.user_coupon.application.mapper.UserCouponResponseMapper;
import com.cinemaebooking.backend.user_coupon.application.port.UserCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetUserCouponListUseCase {

    private final UserCouponRepository userCouponRepository;
    private final UserCouponResponseMapper mapper;

    public Page<UserCouponResponse> execute(Long userId, Pageable pageable) {
        if (userId == null) {
            throw CommonExceptions.invalidInput("User id must not be null");
        }
        if (pageable == null) {
            throw CommonExceptions.invalidInput("Pageable must not be null");
        }
        return userCouponRepository.findByUserId(userId, pageable)
                .map(mapper::toResponse);
    }
}