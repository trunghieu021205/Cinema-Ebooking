package com.cinemaebooking.backend.room_layout.application.dto.roomLayoutSeat;

import com.cinemaebooking.backend.room_layout.domain.enums.SeatStatus;

public record SeatUpdateRequest(
        Long seatId,
        SeatStatus newStatus,
        Long newSeatTypeId
) {}