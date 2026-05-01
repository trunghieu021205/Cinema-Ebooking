package com.cinemaebooking.backend.showtime_seat.application.dto;

import com.cinemaebooking.backend.seat.domain.model.seat.Seat;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ShowtimeSeatResponse {

    private Long seatId;
    private String rowLabel;
    private Integer columnNumber;

    public static ShowtimeSeatResponse from(Seat seat) {
        return ShowtimeSeatResponse.builder()
                .seatId(seat.getId().getValue())
                .rowLabel(seat.getRowLabel())
                .columnNumber(seat.getColumnNumber())
                .build();
    }
}