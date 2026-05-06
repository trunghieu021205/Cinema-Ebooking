package com.cinemaebooking.backend.room_layout.application.usecase.roomLayout;

import com.cinemaebooking.backend.common.exception.ErrorCategory;
import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.common.exception.domain.RoomExceptions;
import com.cinemaebooking.backend.common.exception.domain.RoomLayoutExceptions;
import com.cinemaebooking.backend.room.application.port.RoomRepository;
import com.cinemaebooking.backend.room.domain.model.Room;
import com.cinemaebooking.backend.room.domain.valueObject.RoomId;
import com.cinemaebooking.backend.room_layout.application.port.roomLayout.RoomLayoutRepository;
import com.cinemaebooking.backend.room_layout.application.port.roomLayoutSeat.RoomLayoutSeatRepository;
import com.cinemaebooking.backend.room_layout.application.port.seatType.SeatTypeRepository;
import com.cinemaebooking.backend.room_layout.domain.enums.SeatStatus;
import com.cinemaebooking.backend.room_layout.domain.model.roomLayout.RoomLayout;
import com.cinemaebooking.backend.room_layout.domain.model.roomLayoutSeat.RoomLayoutSeat;
import com.cinemaebooking.backend.room_layout.domain.model.seatType.SeatType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GenerateRoomLayoutUseCase {

    private final RoomRepository roomRepository;
    private final RoomLayoutRepository roomLayoutRepository;
    private final SeatTypeRepository seatTypeRepository;

    @Transactional
    public void execute(RoomId roomId) {
        if (roomId == null) {
            throw CommonExceptions.invalidInput("roomId", ErrorCategory.REQUIRED,"roomId must not be null");
        }

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> RoomExceptions.notFound(roomId));

        boolean alreadyHasLayout = roomLayoutRepository.existsByRoomId(roomId.getValue());
        if (alreadyHasLayout) {
            throw RoomLayoutExceptions.alreadyExists(roomId.getValue());
        }

        RoomLayout layout = buildLayoutWithSeats(room);
        roomLayoutRepository.create(layout);
    }

    private RoomLayout buildLayoutWithSeats(Room room) {
        SeatType standardType = seatTypeRepository.findByNameIgnoreCase("STANDARD")
                .orElseThrow(() -> new IllegalStateException("STANDARD seat type not found"));

        int totalRows = room.getNumberOfRows();
        int totalCols = room.getNumberOfCols();
        List<RoomLayoutSeat> seats = new ArrayList<>();

        for (int r = 0; r < totalRows; r++) {
            for (int c = 0; c < totalCols; c++) {
                int pairIndex = c / 2;
                long coupleGroupId = (long) r * 1000 + pairIndex;

                seats.add(RoomLayoutSeat.builder()
                        .roomLayoutId(null)
                        .rowIndex(r + 1)
                        .colIndex(c + 1)
                        .label(buildLabel(r, c))
                        .status(SeatStatus.ACTIVE)
                        .seatTypeId(standardType.getId().getValue())
                        .coupleGroupId(coupleGroupId)
                        .build());
            }
        }

        return RoomLayout.builder()
                .roomId(room.getId().getValue())
                .layoutVersion(1)
                .effectiveDate(LocalDate.now())
                .totalRows(totalRows)
                .totalCols(totalCols)
                .seats(seats)
                .build();
    }

    private String buildLabel(int rowIndex, int colIndex) {
        char rowChar = (char) ('A' + rowIndex);
        return String.valueOf(rowChar) + (colIndex + 1);
    }
}