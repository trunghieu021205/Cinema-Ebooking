package com.cinemaebooking.backend.user_coupon.application.usecase;

import com.cinemaebooking.backend.user_coupon.application.dto.RedeemCouponRequest;
import com.cinemaebooking.backend.user_coupon.application.dto.UserCouponResponse;
import com.cinemaebooking.backend.user_coupon.application.mapper.UserCouponResponseMapper;
import com.cinemaebooking.backend.user_coupon.application.port.CouponPort;
import com.cinemaebooking.backend.user_coupon.application.port.UserCouponRepository;
import com.cinemaebooking.backend.user_coupon.application.validator.RedeemCouponValidator;
import com.cinemaebooking.backend.user_coupon.domain.model.UserCoupon;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RedeemCouponUseCase {

    private final UserCouponRepository userCouponRepository;
    private final CouponPort couponPort;
    private final UserCouponResponseMapper mapper;
    private final RedeemCouponValidator validator;

    public UserCouponResponse execute(RedeemCouponRequest request) {
        validator.validate(request);

        var coupon = couponPort.findValidCoupon(request.getCouponId(), LocalDateTime.now())
                .orElseThrow(); // already validated

        UserCoupon userCoupon = UserCoupon.redeem(
                request.getUserId(),
                request.getCouponId(),
                LocalDateTime.now(),
                coupon.perUserUsage(),
                coupon.expiryDate()
        );

        UserCoupon saved = userCouponRepository.create(userCoupon);
        return mapper.toResponse(saved);
    }
}