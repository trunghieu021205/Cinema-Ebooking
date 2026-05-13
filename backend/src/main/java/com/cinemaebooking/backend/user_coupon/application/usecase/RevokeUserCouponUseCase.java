package com.cinemaebooking.backend.user_coupon.application.usecase;

import com.cinemaebooking.backend.user_coupon.application.dto.RevokeUserCouponRequest;
import com.cinemaebooking.backend.user_coupon.application.dto.UserCouponResponse;
import com.cinemaebooking.backend.user_coupon.application.mapper.UserCouponResponseMapper;
import com.cinemaebooking.backend.user_coupon.application.port.UserCouponRepository;
import com.cinemaebooking.backend.user_coupon.application.validator.RevokeUserCouponValidator;
import com.cinemaebooking.backend.user_coupon.domain.valueobject.UserCouponId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RevokeUserCouponUseCase {

    private final UserCouponRepository userCouponRepository;
    private final UserCouponResponseMapper mapper;
    private final RevokeUserCouponValidator validator;

    @Transactional
    public UserCouponResponse execute(RevokeUserCouponRequest request) {
        validator.validate(request);

        UserCouponId id = UserCouponId.of(request.getUserCouponId());
        var userCoupon = userCouponRepository.findById(id).orElseThrow();
        userCoupon.revoke();
        var saved = userCouponRepository.update(userCoupon);
        return mapper.toResponse(saved);
    }
}