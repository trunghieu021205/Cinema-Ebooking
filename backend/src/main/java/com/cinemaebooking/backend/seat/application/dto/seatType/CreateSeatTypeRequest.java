package com.cinemaebooking.backend.seat.application.dto.seatType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateSeatTypeRequest {
    private String name;
    private Long basePrice;
}
