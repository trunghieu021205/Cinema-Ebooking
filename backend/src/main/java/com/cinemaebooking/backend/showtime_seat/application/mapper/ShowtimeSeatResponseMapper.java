package com.cinemaebooking.backend.showtime_seat.application.mapper;

import com.cinemaebooking.backend.seat.domain.model.seat.Seat;
import com.cinemaebooking.backend.showtime_seat.application.dto.ShowtimeSeatResponse;
import org.springframework.stereotype.Component;

@Component
public class ShowtimeSeatResponseMapper {

    public ShowtimeSeatResponse toResponse(Seat seat) {

        if (seat == null) return null;

        return ShowtimeSeatResponse.builder()
                .seatId(seat.getId().getValue())
                .rowLabel(seat.getRowLabel())
                .columnNumber(seat.getColumnNumber())
                .build();
    }
}
