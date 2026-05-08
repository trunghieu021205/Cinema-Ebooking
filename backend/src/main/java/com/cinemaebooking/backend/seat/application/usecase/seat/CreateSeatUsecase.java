package com.cinemaebooking.backend.seat.application.usecase.seat;

import com.cinemaebooking.backend.seat.application.dto.seat.CreateSeatRequest;
import com.cinemaebooking.backend.seat.application.dto.seat.SeatResponse;
import com.cinemaebooking.backend.seat.application.mapper.seat.SeatResponseMapper;
import com.cinemaebooking.backend.seat.application.port.seat.SeatRepository;
import com.cinemaebooking.backend.seat.domain.enums.SeatStatus;
import com.cinemaebooking.backend.seat.domain.model.seat.Seat;
import com.cinemaebooking.backend.seat.application.validator.SeatCommandValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * CreateSeatUseCase - Handles creation of a new Seat.
 */
@Service
@RequiredArgsConstructor
public class CreateSeatUsecase {

    private final SeatRepository seatRepository;
    private final SeatResponseMapper mapper;
    private final SeatCommandValidator validator;

    public SeatResponse execute(CreateSeatRequest request) {

        // ================== VALIDATION ==================
        validator.validateCreateRequest(request);

        // ================== BUILD DOMAIN ==================
        Seat seat = buildSeat(request);

        // ================== PERSIST ==================
        Seat saved = seatRepository.create(seat);

        // ================== RESPONSE ==================
        return mapper.toResponse(saved);
    }

    // ================== PRIVATE METHODS ==================

    private Seat buildSeat(CreateSeatRequest request) {
        return Seat.builder()
                .rowIndex(request.getRowIndex())
                .colIndex(request.getColIndex())
                .label(resolveLabel(request))
                .seatTypeId(request.getSeatTypeId())
                .roomId(request.getRoomId())
                .status(SeatStatus.ACTIVE)
                .build();
    }

    private String resolveLabel(CreateSeatRequest request) {
        if (request.getLabel() != null && !request.getLabel().isBlank()) {
            return request.getLabel();
        }
        return buildLabel(request.getRowIndex(), request.getColIndex());
    }

    private String buildLabel(Integer rowIndex, Integer colIndex) {
        char rowChar = (char) ('A' + rowIndex);
        return String.valueOf(rowChar) + (colIndex + 1);
    }
}
