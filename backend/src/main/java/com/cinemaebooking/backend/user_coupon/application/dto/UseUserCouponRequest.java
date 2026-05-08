package com.cinemaebooking.backend.user_coupon.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UseUserCouponRequest {
    private Long userCouponId;   // id của bản ghi UserCoupon
    private Long userId;         // để xác nhận chủ sở hữu
}