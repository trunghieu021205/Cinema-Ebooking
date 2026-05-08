package com.cinemaebooking.backend.showtime_seat.application.mapper;

import com.cinemaebooking.backend.showtime_seat.application.dto.ShowtimeSeatLayoutResponse;
import com.cinemaebooking.backend.showtime_seat.application.dto.ShowtimeSeatResponse;

import com.cinemaebooking.backend.showtime_seat.domain.model.ShowtimeSeat;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ShowtimeSeatLayoutMapper {

    public ShowtimeSeatLayoutResponse toLayoutResponse(List<ShowtimeSeat> showtimeSeats) {
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
        List<List<ShowtimeSeatResponse>> rows = new ArrayList<>(maxRow + 1);
        for (int i = 0; i <= maxRow; i++) {
            List<ShowtimeSeatResponse> row = new ArrayList<>(maxCol + 1);
            for (int j = 0; j <= maxCol; j++) {
                row.add(null);
            }
            rows.add(row);
        }

        for (ShowtimeSeat showtimeSeat : showtimeSeats) {
            int rowIdx = showtimeSeat.getRowIndex();
            int colIdx = showtimeSeat.getColIndex();

            String status = showtimeSeat.getStatus().name().toLowerCase();
            if (!showtimeSeat.isActive()) {
                status = "disabled";
            }

            ShowtimeSeatResponse response = ShowtimeSeatResponse.builder()
                    .roomLayoutSeatId(showtimeSeat.getRoomLayoutSeatId())
                    .seatNumber(showtimeSeat.getSeatNumber())
                    .rowIndex(rowIdx)
                    .colIndex(colIdx)
                    .seatTypeId(showtimeSeat.getSeatTypeId())
                    .status(status)
                    .build();

            rows.get(rowIdx).set(colIdx, response);
        }

        return ShowtimeSeatLayoutResponse.builder()
                .rows(rows)
                .totalRows(maxRow + 1)
                .totalCols(maxCol + 1)
                .build();
    }
}