package com.cinemaebooking.backend.seat.application.dto.seatType;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SeatTypeResponse {

    private Long id;
    private String name;
    private Long basePrice;
}
