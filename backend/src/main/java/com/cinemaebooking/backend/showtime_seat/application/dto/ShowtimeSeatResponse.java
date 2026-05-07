package com.cinemaebooking.backend.showtime_seat.application.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ShowtimeSeatResponse {
    private Long roomLayoutSeatId;
    private String label;
    private Integer rowIndex;
    private Integer colIndex;
    private Long seatTypeId;
    private String status;
}