package com.cinemaebooking.backend.room_layout.application.dto.roomLayoutSeat;

import java.time.LocalDate;
import java.util.List;

public record UpdateRoomLayoutSeatsRequest(
        LocalDate effectiveDate,
        List<SeatUpdateRequest> updates
) {}