package com.cinemaebooking.backend.room_layout.application.dto.seatType;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class SeatTypeResponse {

    private Long id;
    private String name;
    private BigDecimal basePrice;
}
