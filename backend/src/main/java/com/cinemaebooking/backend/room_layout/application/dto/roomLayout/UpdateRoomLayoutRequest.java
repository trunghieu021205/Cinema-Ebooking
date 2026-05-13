package com.cinemaebooking.backend.room_layout.application.dto.roomLayout;

import com.cinemaebooking.backend.room.domain.enums.RoomType;
import com.cinemaebooking.backend.room_layout.application.dto.roomLayoutSeat.SeatUpdateRequest;

import java.time.LocalDate;
import java.util.List;

public record UpdateRoomLayoutRequest(
        LocalDate effectiveDate,
        RoomType roomType,
        List<SeatUpdateRequest> updates
) {}