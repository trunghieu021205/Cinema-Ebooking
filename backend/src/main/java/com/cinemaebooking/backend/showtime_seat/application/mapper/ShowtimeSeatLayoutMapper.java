package com.cinemaebooking.backend.showtime_seat.application.mapper;

import com.cinemaebooking.backend.showtime.domain.valueobject.ShowtimeId;
import com.cinemaebooking.backend.showtime_seat.application.dto.ShowtimeSeatLayoutResponse;
import com.cinemaebooking.backend.showtime_seat.application.dto.ShowtimeSeatResponse;

import com.cinemaebooking.backend.showtime_seat.domain.model.ShowtimeSeat;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ShowtimeSeatLayoutMapper {

    public ShowtimeSeatLayoutResponse toLayoutResponse(List<ShowtimeSeat> showtimeSeats, ShowtimeId showtimeId) {
        if (showtimeSeats == null || showtimeSeats.isEmpty()) {
            return ShowtimeSeatLayoutResponse.builder()
                    .rows(Collections.emptyList())
                    .totalRows(0)
                    .totalCols(0)
                    .build();
        }

        // Lấy kích thước lưới từ chính ShowtimeSeat
        int maxRow = showtimeSeats.stream()
                .mapToInt(ShowtimeSeat::getRowIndex)
                .max().orElse(0);
        int maxCol = showtimeSeats.stream()
                .mapToInt(ShowtimeSeat::getColIndex)
                .max().orElse(0);

        // Khởi tạo ma trận rows
        List<List<ShowtimeSeatResponse>> rows = new ArrayList<>(maxRow );
        for (int i = 0; i < maxRow; i++) {
            List<ShowtimeSeatResponse> row = new ArrayList<>(maxCol );
            for (int j = 0; j < maxCol; j++) {
                row.add(null);
            }
            rows.add(row);
        }

        for (ShowtimeSeat showtimeSeat : showtimeSeats) {
            int rowIdx = showtimeSeat.getRowIndex();
            int colIdx = showtimeSeat.getColIndex();

            ShowtimeSeatResponse response = ShowtimeSeatResponse.builder()
                    .id(showtimeSeat.getId().getValue())
                    .roomLayoutSeatId(showtimeSeat.getRoomLayoutSeatId())
                    .seatNumber(showtimeSeat.getSeatNumber())
                    .rowIndex(rowIdx)
                    .colIndex(colIdx)
                    .isActive(showtimeSeat.isActive())
                    .seatTypeId(showtimeSeat.getSeatTypeId())
                    .status(showtimeSeat.getStatus())
                    .price(showtimeSeat.getPrice())
                    .build();

            rows.get(rowIdx - 1).set(colIdx - 1, response);
        }

        return ShowtimeSeatLayoutResponse.builder()
                .showtimeId(showtimeId.getValue())
                .rows(rows)
                .totalRows(maxRow)
                .totalCols(maxCol)
                .build();
    }
}