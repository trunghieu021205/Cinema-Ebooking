package com.cinemaebooking.backend.coupon.presentation;

import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.coupon.application.dto.CouponResponse;
import com.cinemaebooking.backend.coupon.application.dto.CreateCouponRequest;
import com.cinemaebooking.backend.coupon.application.dto.UpdateCouponRequest;
import com.cinemaebooking.backend.coupon.application.usecase.*;
import com.cinemaebooking.backend.coupon.domain.valueobject.CouponId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CreateCouponUseCase createCouponUseCase;
    private final UpdateCouponUseCase updateCouponUseCase;
    private final DeleteCouponUseCase deleteCouponUseCase;
    private final GetCouponDetailUseCase getCouponDetailUseCase;
    private final GetCouponListUseCase getCouponListUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CouponResponse createCoupon(@RequestBody CreateCouponRequest request) {
        return createCouponUseCase.execute(request);
    }

    @PutMapping("/{id}")
    public CouponResponse updateCoupon(@PathVariable Long id,
                                       @RequestBody UpdateCouponRequest request) {
        CouponId couponId = toCouponId(id);
        return updateCouponUseCase.execute(couponId, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCoupon(@PathVariable Long id) {
        CouponId couponId = toCouponId(id);
        deleteCouponUseCase.execute(couponId);
    }

    @GetMapping("/{id}")
    public CouponResponse getCouponDetail(@PathVariable Long id) {
        CouponId couponId = toCouponId(id);
        return getCouponDetailUseCase.execute(couponId);
    }

    @GetMapping
    public Page<CouponResponse> getCouponList(@PageableDefault(size = 10) Pageable pageable) {
        return getCouponListUseCase.execute(pageable);
    }

    private CouponId toCouponId(Long id) {
        if (id == null) {
            throw CommonExceptions.invalidInput("Coupon id must not be null");
        }
        return CouponId.of(id);
    }
}