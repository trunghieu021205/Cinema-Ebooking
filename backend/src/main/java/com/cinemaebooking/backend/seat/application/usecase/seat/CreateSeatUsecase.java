package com.cinemaebooking.backend.seat.application.usecase.seat;

import com.cinemaebooking.backend.seat.application.dto.seat.CreateSeatRequest;
import com.cinemaebooking.backend.seat.application.dto.seat.SeatResponse;
import com.cinemaebooking.backend.seat.application.mapper.seat.SeatResponseMapper;
import com.cinemaebooking.backend.seat.application.port.seat.SeatRepository;
import com.cinemaebooking.backend.seat.domain.enums.SeatStatus;
import com.cinemaebooking.backend.seat.domain.model.seat.Seat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateSeatUsecase {

    private final SeatRepository repository;
    private final SeatResponseMapper mapper;

    public SeatResponse execute(CreateSeatRequest request) {

        // Không cho tạo trùng ghế trong cùng 1 phòng
        if (repository.existsByRoomIdAndRowLabelAndColumnNumber(
                request.getRoomId(),
                request.getRowLabel(),
                request.getColumnNumber()
        )) {
            throw new IllegalArgumentException("Seat already exists in this room");
        }

        Seat seat = Seat.builder()
                .rowLabel(request.getRowLabel())
                .columnNumber(request.getColumnNumber())
                .status(SeatStatus.AVAILABLE)
                .seatTypeId(request.getSeatTypeId())
                .roomId(request.getRoomId())
                .build();

        return mapper.toResponse(repository.create(seat));
    }
}