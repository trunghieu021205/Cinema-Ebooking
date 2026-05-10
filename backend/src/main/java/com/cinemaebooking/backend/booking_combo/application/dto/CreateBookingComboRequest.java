package com.cinemaebooking.backend.booking_combo.application.dto;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookingComboRequest {
    private Long comboId;
    private String comboName;
    private BigDecimal unitPrice;
    private Integer quantity;
}
