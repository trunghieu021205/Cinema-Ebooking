package com.cinemaebooking.backend.showtime_seat.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class ShowtimeSeatLayoutResponse {
    List<List<ShowtimeSeatResponse>> rows;
    int totalRows;
    int totalCols;
}
