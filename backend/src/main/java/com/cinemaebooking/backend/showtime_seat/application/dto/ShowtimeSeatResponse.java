package com.cinemaebooking.backend.showtime_seat.application.dto;

import com.cinemaebooking.backend.seat.domain.model.seat.Seat;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ShowtimeSeatResponse {
    private Long seatId;
    private String label;
    private Integer rowIndex;
    private Integer colIndex;
    private Long seatTypeId;
    private String status;
}