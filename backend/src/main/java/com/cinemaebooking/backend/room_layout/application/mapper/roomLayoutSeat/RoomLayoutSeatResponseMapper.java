package com.cinemaebooking.backend.room_layout.application.mapper.roomLayoutSeat;

import com.cinemaebooking.backend.room_layout.application.dto.roomLayoutSeat.RoomLayoutSeatResponse;
import com.cinemaebooking.backend.room_layout.domain.model.roomLayoutSeat.RoomLayoutSeat;
import org.springframework.stereotype.Component;

@Component
public class RoomLayoutSeatResponseMapper {

    public RoomLayoutSeatResponse toResponse(RoomLayoutSeat roomLayoutSeat) {
        if (roomLayoutSeat == null) return null;

        return RoomLayoutSeatResponse.builder().
                id(roomLayoutSeat.getId() != null ? roomLayoutSeat.getId().getValue() : null)
                .rowIndex(roomLayoutSeat.getRowIndex())
                .colIndex(roomLayoutSeat.getColIndex())
                .label(roomLayoutSeat.getLabel())
                .status(roomLayoutSeat.getStatus())
                .seatTypeId(roomLayoutSeat.getSeatTypeId())
                .roomLayoutId(roomLayoutSeat.getRoomLayoutId())
                .coupleGroupId(roomLayoutSeat.getCoupleGroupId())
                .build();
    }
}

