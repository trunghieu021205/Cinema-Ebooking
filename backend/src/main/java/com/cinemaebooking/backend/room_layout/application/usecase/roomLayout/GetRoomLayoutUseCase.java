package com.cinemaebooking.backend.room_layout.application.usecase.roomLayout;

import com.cinemaebooking.backend.common.exception.ErrorCategory;
import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.common.exception.domain.RoomLayoutExceptions;
import com.cinemaebooking.backend.room_layout.application.dto.roomLayout.RoomLayoutDetailResponse;
import com.cinemaebooking.backend.room_layout.application.dto.roomLayoutSeat.RoomLayoutSeatResponse;
import com.cinemaebooking.backend.room_layout.application.mapper.roomLayoutSeat.RoomLayoutSeatResponseMapper;
import com.cinemaebooking.backend.room_layout.application.port.roomLayout.RoomLayoutRepository;
import com.cinemaebooking.backend.room_layout.domain.model.roomLayout.RoomLayout;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetRoomLayoutUseCase {

    private final RoomLayoutRepository roomLayoutRepository;
    private final RoomLayoutSeatResponseMapper mapper;

    public RoomLayoutDetailResponse execute(Long roomId) {
        if (roomId == null) {
            throw CommonExceptions.invalidInput("roomId", ErrorCategory.REQUIRED,"roomId must not be null");
        }

        RoomLayout currentLayout = roomLayoutRepository.findCurrentByRoomIdAndDate(roomId, LocalDate.now())
                .orElseThrow(() -> RoomLayoutExceptions.noCurrentLayout(roomId, LocalDate.now()));

        List<RoomLayoutSeatResponse> seatResponses = currentLayout.getSeats()
                .stream()
                .map(mapper::toResponse)
                .toList();

        return buildRoomLayout(seatResponses, currentLayout.getTotalRows(), currentLayout.getTotalCols());
    }

    private RoomLayoutDetailResponse buildRoomLayout(List<RoomLayoutSeatResponse> seats, int totalRows, int totalCols) {
        List<List<RoomLayoutSeatResponse>> grid = new ArrayList<>(totalRows);
        for (int r = 1; r <= totalRows; r++) {
            int rowIndex = r;
            List<RoomLayoutSeatResponse> row = seats.stream()
                    .filter(seat -> seat.getRowIndex() == rowIndex)
                    .sorted(Comparator.comparing(RoomLayoutSeatResponse::getColIndex))
                    .toList();
            grid.add(row);
        }
        return RoomLayoutDetailResponse.builder()
                .rows(grid)
                .totalRows(totalRows)
                .totalCols(totalCols)
                .build();
    }
}