package com.cinemaebooking.backend.seat.application.usecase.seat;

import com.cinemaebooking.backend.seat.application.dto.seat.SeatResponse;
import com.cinemaebooking.backend.seat.application.dto.seat.UpdateSeatRequest;
import com.cinemaebooking.backend.seat.application.mapper.seat.SeatResponseMapper;
import com.cinemaebooking.backend.seat.application.port.seat.SeatRepository;
import com.cinemaebooking.backend.seat.domain.model.seat.Seat;
import com.cinemaebooking.backend.seat.domain.valueObject.seat.SeatId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateSeatUsecase {

    private final SeatRepository repository;
    private final SeatResponseMapper mapper;

    public SeatResponse execute(Long id, UpdateSeatRequest request) {

        // 1. Lấy seat cũ
        Seat oldSeat = repository.findById(new SeatId(id))
                .orElseThrow(() -> new RuntimeException("Seat not found"));

        // 2. Chỉ check duplicate nếu vị trí thay đổi
        boolean isSamePosition =
                oldSeat.getRowLabel().equals(request.getRowLabel()) &&
                        oldSeat.getColumnNumber().equals(request.getColumnNumber());

        if (!isSamePosition &&
                repository.existsByRoomIdAndRowLabelAndColumnNumberAndIdNot(
                        oldSeat.getRoomId(),
                        request.getRowLabel(),
                        request.getColumnNumber(),
                        id
                )) {
            throw new IllegalArgumentException("Seat already exists in this room");
        }

        // 3. Update
        Seat updatedSeat = Seat.builder()
                .id(new SeatId(id))
                .rowLabel(request.getRowLabel())
                .columnNumber(request.getColumnNumber())
                .status(request.getSeatStatus())
                .seatTypeId(request.getSeatTypeId())
                .roomId(oldSeat.getRoomId())
                .build();

        return mapper.toResponse(repository.update(updatedSeat));
    }
}

