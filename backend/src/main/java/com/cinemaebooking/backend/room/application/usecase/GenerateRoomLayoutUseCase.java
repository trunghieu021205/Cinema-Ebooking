package com.cinemaebooking.backend.room.application.usecase;

import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.common.exception.domain.RoomExceptions;
import com.cinemaebooking.backend.room.application.port.RoomRepository;
import com.cinemaebooking.backend.room.domain.model.Room;
import com.cinemaebooking.backend.room.domain.valueObject.RoomId;
import com.cinemaebooking.backend.seat.application.port.seat.SeatRepository;
import com.cinemaebooking.backend.seat.application.port.seatType.SeatTypeRepository;
import com.cinemaebooking.backend.seat.domain.enums.SeatStatus;
import com.cinemaebooking.backend.seat.domain.model.seat.Seat;
import com.cinemaebooking.backend.seat.domain.model.seatType.SeatType;
import com.cinemaebooking.backend.seat.domain.valueObject.seatType.SeatTypeId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * GenerateRoomLayoutUseCase - Generate default seat grid for a room.
 * Flow:
 * 1. Validate room exists
 * 2. Check layout not already generated
 * 3. Generate seats: rows × cols, all ACTIVE, default SeatType
 * 4. Persist batch
 */
@Service
@RequiredArgsConstructor
public class GenerateRoomLayoutUseCase {

    private final RoomRepository roomRepository;
    private final SeatRepository seatRepository;
    private final SeatTypeRepository seatTypeRepository;

    @Transactional
    public void execute(RoomId roomId) {

        // ================== VALIDATION ==================
        if (roomId == null) {
            throw CommonExceptions.invalidInput("Room id must not be null");
        }

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> RoomExceptions.notFound(roomId));

        // ================== BUSINESS CHECK ==================
        boolean alreadyGenerated = seatRepository.existsByRoomId(roomId.getValue());
        if (alreadyGenerated) {
            throw CommonExceptions.invalidInput("Layout already generated for this room");
        }

        // ================== GENERATE SEATS ==================
        List<Seat> seats = buildDefaultLayout(room);

        // ================== PERSIST ==================
        seatRepository.createBatch(seats);
    }

    // ================== PRIVATE METHODS ==================

    private List<Seat> buildDefaultLayout(Room room) {
        List<Seat> seats = new ArrayList<>();

        SeatType standardType = seatTypeRepository.findByNameIgnoreCase("STANDARD")
                .orElseThrow(() -> new IllegalStateException("STANDARD seat type not found"));


        for (int r = 0; r < room.getNumberOfRows(); r++) {
            for (int c = 0; c < room.getNumberOfCols(); c++) {
                seats.add(Seat.builder()
                        .roomId(room.getId().getValue())
                        .rowIndex(r)
                        .colIndex(c)
                        .label(buildLabel(r, c))
                        .status(SeatStatus.ACTIVE)
                        .seatTypeId(standardType.getId().getValue()) // default: chưa assign type
                        .build());
            }
        }

        return seats;
    }

    /**
     * Label format: A1, A2 ... B1, B2 ...
     * Row 0 = A, Row 1 = B, ...
     */
    private String buildLabel(int rowIndex, int colIndex) {
        char rowChar = (char) ('A' + rowIndex);
        return String.valueOf(rowChar) + (colIndex + 1);
    }
}