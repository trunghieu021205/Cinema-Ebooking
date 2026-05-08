package com.cinemaebooking.backend.room.application.usecase;

import com.cinemaebooking.backend.room.application.dto.RoomLayoutResponse;
import com.cinemaebooking.backend.seat.application.dto.seat.SeatResponse;
import com.cinemaebooking.backend.seat.application.mapper.seat.SeatResponseMapper;
import com.cinemaebooking.backend.seat.application.port.seat.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetRoomLayoutUseCase {

    private final SeatRepository seatRepository;
    private final SeatResponseMapper mapper;

    public RoomLayoutResponse execute(Long roomId) {

        if (roomId == null) {
            throw new IllegalArgumentException("RoomId must not be null");
        }

        List<SeatResponse> seats = seatRepository.findByRoomId(roomId)
                .stream()
                .map(mapper::toResponse)
                .toList();

        return buildRoomLayout(seats);
    }

    private RoomLayoutResponse buildRoomLayout(List<SeatResponse> seats) {

        int maxRow = seats.stream()
                .mapToInt(SeatResponse::getRowIndex)
                .max()
                .orElse(0);

        int maxCol = seats.stream()
                .mapToInt(SeatResponse::getColIndex)
                .max()
                .orElse(0);

        List<List<SeatResponse>> grid = new ArrayList<>();

        for (int r = 0; r <= maxRow; r++) {

            int rowIndex = r;

            List<SeatResponse> row = seats.stream()
                    .filter(seat -> seat.getRowIndex() == rowIndex)
                    .sorted(Comparator.comparing(SeatResponse::getColIndex))
                    .toList();

            grid.add(row);
        }

        return RoomLayoutResponse.builder()
                .rows(grid)
                .totalRows(maxRow + 1)
                .totalCols(maxCol + 1)
                .build();
    }
}
