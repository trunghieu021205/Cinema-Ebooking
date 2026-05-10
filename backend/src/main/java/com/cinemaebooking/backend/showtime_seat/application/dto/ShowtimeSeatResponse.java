package com.cinemaebooking.backend.showtime_seat.application.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ShowtimeSeatResponse {
    private Long roomLayoutSeatId;
    private String seatNumber;
    private Integer rowIndex;
    private Integer colIndex;
    private Long seatTypeId;
    private boolean isActive;
    private String status;
}