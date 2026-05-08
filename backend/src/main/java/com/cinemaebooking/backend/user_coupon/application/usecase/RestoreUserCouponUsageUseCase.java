package com.cinemaebooking.backend.user_coupon.application.usecase;

import com.cinemaebooking.backend.user_coupon.application.dto.RestoreUserCouponRequest;
import com.cinemaebooking.backend.user_coupon.application.dto.UserCouponResponse;
import com.cinemaebooking.backend.user_coupon.application.mapper.UserCouponResponseMapper;
import com.cinemaebooking.backend.user_coupon.application.port.UserCouponRepository;
import com.cinemaebooking.backend.user_coupon.application.validator.RestoreUserCouponValidator;
import com.cinemaebooking.backend.user_coupon.domain.valueobject.UserCouponId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RestoreUserCouponUsageUseCase {

    private final UserCouponRepository userCouponRepository;
    private final UserCouponResponseMapper mapper;
    private final RestoreUserCouponValidator validator;

    @Transactional
    public UserCouponResponse execute(RestoreUserCouponRequest request) {
        validator.validate(request);

        UserCouponId id = UserCouponId.of(request.getUserCouponId());
        var userCoupon = userCouponRepository.findById(id).orElseThrow();
        userCoupon.restoreUsage(LocalDateTime.now());
        var saved = userCouponRepository.update(userCoupon);
        return mapper.toResponse(saved);
    }
}