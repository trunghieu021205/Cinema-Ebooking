package com.cinemaebooking.backend.booking_coupon.application.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingCouponResponse {
    private Long id;
    private Long bookingId;
    private Long userCouponId;
    private String code;
    private BigDecimal discountValue;
    private LocalDateTime appliedAt;
}
