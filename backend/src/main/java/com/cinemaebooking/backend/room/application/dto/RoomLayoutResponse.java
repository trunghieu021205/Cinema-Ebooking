package com.cinemaebooking.backend.room.application.dto;

import com.cinemaebooking.backend.seat.application.dto.seat.SeatResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class RoomLayoutResponse {
    List<List<SeatResponse>> rows;
    int totalRows;
    int totalCols;
}
