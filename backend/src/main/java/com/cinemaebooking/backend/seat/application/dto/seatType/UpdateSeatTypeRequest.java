package com.cinemaebooking.backend.seat.application.dto.seatType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSeatTypeRequest {
    private String name;
    private Long basePrice;
}