package com.cinemaebooking.backend.user_coupon.application.dto;

import com.cinemaebooking.backend.user_coupon.domain.enums.UserCouponStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UserCouponResponse {
    private Long id;
    private Long userId;
    private Long couponId;
    private LocalDateTime receivedAt;
    private Integer usageRemain;
    private LocalDateTime usedAt;
    private LocalDateTime expiredAt;
    private UserCouponStatus status;
}