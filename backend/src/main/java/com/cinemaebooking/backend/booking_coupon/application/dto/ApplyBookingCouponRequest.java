package com.cinemaebooking.backend.booking_coupon.application.dto;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplyBookingCouponRequest {
    private Long userCouponId;
    private String code;
    private BigDecimal discountValue;
}
