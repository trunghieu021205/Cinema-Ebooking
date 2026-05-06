package com.cinemaebooking.backend.showtime_seat.application.mapper;

import com.cinemaebooking.backend.room_layout.domain.model.roomLayoutSeat.RoomLayoutSeat;
import com.cinemaebooking.backend.showtime_seat.application.dto.ShowtimeSeatLayoutResponse;
import com.cinemaebooking.backend.showtime_seat.application.dto.ShowtimeSeatResponse;
import com.cinemaebooking.backend.showtime_seat.domain.enums.ShowtimeSeatStatus;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ShowtimeSeatLayoutMapper {

    public ShowtimeSeatLayoutResponse toLayoutResponse(
            List<RoomLayoutSeat> allSeatsInLayout,
            Map<Long, ShowtimeSeatStatus> seatIdToStatus
    ) {
        if (allSeatsInLayout == null || allSeatsInLayout.isEmpty()) {
            return ShowtimeSeatLayoutResponse.builder()
                    .rows(Collections.emptyList())
                    .totalRows(0)
                    .totalCols(0)
                    .build();
        }

        int maxRow = allSeatsInLayout.stream()
                .mapToInt(RoomLayoutSeat::getRowIndex)
                .max().orElse(0);
        int maxCol = allSeatsInLayout.stream()
                .mapToInt(RoomLayoutSeat::getColIndex)
                .max().orElse(0);

        List<List<ShowtimeSeatResponse>> rows = new ArrayList<>(maxRow + 1);
        for (int i = 0; i <= maxRow; i++) {
            List<ShowtimeSeatResponse> row = new ArrayList<>(maxCol + 1);
            for (int j = 0; j <= maxCol; j++) {
                row.add(null);
            }
            rows.add(row);
        }

        for (RoomLayoutSeat seat : allSeatsInLayout) {
            int rowIdx = seat.getRowIndex();
            int colIdx = seat.getColIndex();

            ShowtimeSeatStatus domainStatus = seatIdToStatus.get(seat.getId().getValue());
            String finalStatus = (domainStatus != null)
                    ? domainStatus.name().toLowerCase()
                    : "available";

            if (!seat.isActive()) {
                finalStatus = "disabled";
            }

            ShowtimeSeatResponse response = ShowtimeSeatResponse.builder()
                    .roomLayoutSeatId(seat.getId().getValue())
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