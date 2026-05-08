package com.cinemaebooking.backend.room_layout.application.dto.roomLayoutSeat;

import com.cinemaebooking.backend.room_layout.domain.enums.SeatStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class RoomLayoutSeatResponse {

    Long id;
    Integer rowIndex;
    Integer colIndex;
    SeatStatus status;
    Long seatTypeId;
    Long roomLayoutId;
    Long coupleGroupId;

}
