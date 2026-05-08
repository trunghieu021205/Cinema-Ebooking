package com.cinemaebooking.backend.room_layout.application.helper;

import com.cinemaebooking.backend.room_layout.application.dto.roomLayoutSeat.RoomLayoutSeatResponse;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SeatGridBuilder {

    public static List<List<RoomLayoutSeatResponse>> build(
            List<RoomLayoutSeatResponse> seats, int totalRows, int totalCols) {
        List<List<RoomLayoutSeatResponse>> grid = new ArrayList<>(totalRows);
        for (int r = 1; r <= totalRows; r++) {
            int rowIndex = r;
            List<RoomLayoutSeatResponse> row = seats.stream()
                    .filter(seat -> seat.getRowIndex() == rowIndex)
                    .sorted(Comparator.comparing(RoomLayoutSeatResponse::getColIndex))
                    .toList();
            grid.add(row);
        }
        return grid;
    }
}