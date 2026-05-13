package com.cinemaebooking.backend.booking_combo.application.dto;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingComboResponse {
    private Long id;
    private Long bookingId;
    private Long comboId;
    private String comboName;
    private BigDecimal unitPrice;
    private Integer quantity;
    private BigDecimal totalPrice;
}