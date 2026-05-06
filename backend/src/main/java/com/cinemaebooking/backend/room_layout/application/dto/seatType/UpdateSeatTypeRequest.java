package com.cinemaebooking.backend.room_layout.application.dto.seatType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSeatTypeRequest {
    private String name;
    private BigDecimal basePrice;
}