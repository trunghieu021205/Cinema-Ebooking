package com.cinemaebooking.backend.room_layout.application.dto.seatType;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateSeatTypeRequest {
    private String name;
    private BigDecimal basePrice;
}
