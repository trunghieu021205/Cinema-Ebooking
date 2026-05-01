package com.cinemaebooking.backend.seat.application.mapper.seat;

import com.cinemaebooking.backend.seat.application.dto.seat.SeatResponse;
import com.cinemaebooking.backend.seat.domain.model.seat.Seat;
import org.springframework.stereotype.Component;

@Component
public class SeatResponseMapper {

    public SeatResponse toResponse(Seat seat) {
        if (seat == null) return null;

        return SeatResponse.builder().
                id(seat.getId() != null ? seat.getId().getValue() : null)
                .rowIndex(seat.getRowIndex())
                .colIndex(seat.getColIndex())
                .label(seat.getLabel())
                .status(seat.getStatus())
                .seatTypeId(seat.getSeatTypeId())
                .roomId(seat.getRoomId())
                .build();
    }
}

