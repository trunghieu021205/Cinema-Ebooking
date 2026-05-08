package com.cinemaebooking.backend.showtime_seat.application.mapper;

import com.cinemaebooking.backend.seat.domain.model.seat.Seat;
import com.cinemaebooking.backend.showtime_seat.application.dto.ShowtimeSeatLayoutResponse;
import com.cinemaebooking.backend.showtime_seat.application.dto.ShowtimeSeatResponse;
import com.cinemaebooking.backend.showtime_seat.domain.enums.ShowtimeSeatStatus;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ShowtimeSeatLayoutMapper {

    public ShowtimeSeatLayoutResponse toLayoutResponse(
            List<Seat> allSeatsInRoom,
            Map<Long, ShowtimeSeatStatus> seatIdToStatus
    ) {
        if (allSeatsInRoom == null || allSeatsInRoom.isEmpty()) {
            return ShowtimeSeatLayoutResponse.builder()
                    .rows(Collections.emptyList())
                    .totalRows(0)
                    .totalCols(0)
                    .build();
        }

        // Tìm kích thước ma trận
        int maxRow = allSeatsInRoom.stream()
                .mapToInt(Seat::getRowIndex)
                .max().orElse(0);
        int maxCol = allSeatsInRoom.stream()
                .mapToInt(Seat::getColIndex)
                .max().orElse(0);

        // Khởi tạo ma trận với các ô null
        List<List<ShowtimeSeatResponse>> rows = new ArrayList<>(maxRow + 1);
        for (int i = 0; i <= maxRow; i++) {
            List<ShowtimeSeatResponse> row = new ArrayList<>(maxCol + 1);
            for (int j = 0; j <= maxCol; j++) {
                row.add(null);
            }
            rows.add(row);
        }

        // Điền ghế vào ma trận
        for (Seat seat : allSeatsInRoom) {
            int rowIdx = seat.getRowIndex();
            int colIdx = seat.getColIndex();

            // Xác định status
            ShowtimeSeatStatus domainStatus = seatIdToStatus.get(seat.getId().getValue());
            String finalStatus = (domainStatus != null)
                    ? domainStatus.name().toLowerCase()
                    : "available";

            if (!seat.isActive()) {
                finalStatus = "disabled";
            }

            ShowtimeSeatResponse response = ShowtimeSeatResponse.builder()
                    .seatId(seat.getId().getValue())
                    .label(seat.getLabel())
                    .rowIndex(rowIdx)
                    .colIndex(colIdx)
                    .seatTypeId(seat.getSeatTypeId())
                    .status(finalStatus)
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