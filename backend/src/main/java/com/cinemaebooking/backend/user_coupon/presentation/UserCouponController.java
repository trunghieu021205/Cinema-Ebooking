package com.cinemaebooking.backend.user_coupon.presentation;

import com.cinemaebooking.backend.user_coupon.application.dto.RedeemCouponRequest;
import com.cinemaebooking.backend.user_coupon.application.dto.UserCouponResponse;
import com.cinemaebooking.backend.user_coupon.application.usecase.GetUserCouponDetailUseCase;
import com.cinemaebooking.backend.user_coupon.application.usecase.GetUserCouponListUseCase;
import com.cinemaebooking.backend.user_coupon.application.usecase.RedeemCouponUseCase;
import com.cinemaebooking.backend.user_coupon.domain.valueobject.UserCouponId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user-coupons")
@RequiredArgsConstructor
public class UserCouponController {

    private final RedeemCouponUseCase redeemCouponUseCase;
    private final GetUserCouponListUseCase getListUseCase;
    private final GetUserCouponDetailUseCase getDetailUseCase;

    @PostMapping("/redeem")
    @ResponseStatus(HttpStatus.CREATED)
    public UserCouponResponse redeem(@RequestBody RedeemCouponRequest request) {
        return redeemCouponUseCase.execute(request);
    }

    @GetMapping
    public Page<UserCouponResponse> list(@RequestParam Long userId,
                                         @PageableDefault(size = 10) Pageable pageable) {
        return getListUseCase.execute(userId, pageable);
    }

    @GetMapping("/{id}")
    public UserCouponResponse detail(@PathVariable Long id) {
        return getDetailUseCase.execute(UserCouponId.of(id));
    }
}