package com.cinemaebooking.backend.seat.application.dto.seat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateSeatRequest {
    private String rowLabel;
    private Integer columnNumber;
    private Long seatTypeId;
    private Long roomId;
}

