package com.cinemaebooking.backend.user_coupon.presentation;

import com.cinemaebooking.backend.user_coupon.application.dto.*;
import com.cinemaebooking.backend.user_coupon.application.usecase.*;
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
    private final UseUserCouponUseCase useCouponUseCase;
    private final RestoreUserCouponUsageUseCase restoreUsageUseCase;
    private final ExpireUserCouponsUseCase expireUserCouponsUseCase;
    private final RevokeUserCouponUseCase revokeUserCouponUseCase;

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

    @PostMapping("/use")
    public UserCouponResponse use(@RequestBody UseUserCouponRequest request) {
        return useCouponUseCase.execute(request);
    }

    @PostMapping("/restore")
    public UserCouponResponse restore(@RequestBody RestoreUserCouponRequest request) {
        return restoreUsageUseCase.execute(request);
    }

    @PostMapping("/expire")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void expire() {
        expireUserCouponsUseCase.execute();
    }

    @PostMapping("/revoke")
    public UserCouponResponse revoke(@RequestBody RevokeUserCouponRequest request) {
        return revokeUserCouponUseCase.execute(request);
    }
}