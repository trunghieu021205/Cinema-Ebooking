package com.cinemaebooking.backend.user_coupon.application.usecase;

import com.cinemaebooking.backend.user_coupon.application.dto.UseUserCouponRequest;
import com.cinemaebooking.backend.user_coupon.application.dto.UserCouponResponse;
import com.cinemaebooking.backend.user_coupon.application.mapper.UserCouponResponseMapper;
import com.cinemaebooking.backend.user_coupon.application.port.UserCouponRepository;
import com.cinemaebooking.backend.user_coupon.application.validator.UseUserCouponValidator;
import com.cinemaebooking.backend.user_coupon.domain.valueobject.UserCouponId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UseUserCouponUseCase {

    private final UserCouponRepository userCouponRepository;
    private final UserCouponResponseMapper mapper;
    private final UseUserCouponValidator validator;

    @Transactional
    public UserCouponResponse execute(UseUserCouponRequest request) {
        validator.validate(request);

        UserCouponId id = UserCouponId.of(request.getUserCouponId());
        var userCoupon = userCouponRepository.findById(id).orElseThrow();
        userCoupon.use();
        var saved = userCouponRepository.update(userCoupon);
        return mapper.toResponse(saved);
    }
}