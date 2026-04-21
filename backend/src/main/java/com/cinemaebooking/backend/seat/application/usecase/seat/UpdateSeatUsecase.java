package com.cinemaebooking.backend.seat.application.usecase.seat;

import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.common.exception.domain.SeatExceptions;
import com.cinemaebooking.backend.seat.application.dto.seat.SeatResponse;
import com.cinemaebooking.backend.seat.application.dto.seat.UpdateSeatRequest;
import com.cinemaebooking.backend.seat.application.mapper.seat.SeatResponseMapper;
import com.cinemaebooking.backend.seat.application.port.seat.SeatRepository;
import com.cinemaebooking.backend.seat.domain.model.seat.Seat;
import com.cinemaebooking.backend.seat.application.validator.SeatCommandValidator;
import com.cinemaebooking.backend.seat.domain.valueObject.seat.SeatId;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateSeatUsecase {

    private final SeatRepository seatRepository;
    private final SeatResponseMapper mapper;
    private final SeatCommandValidator validator;

    public SeatResponse execute(SeatId id, UpdateSeatRequest request) {

        // ================== VALIDATION ==================
        validator.validateUpdateRequest(id, request);

        // ================== LOAD ==================
        Seat seat = loadSeat(id);

        // ================== APPLY DOMAIN ==================
        applyUpdate(seat, request);

        // ================== PERSIST ==================
        Seat saved = persist(seat);

        // ================== MAP RESPONSE ==================
        return mapper.toResponse(saved);
    }

    // ================== PRIVATE METHODS ==================

    private Seat loadSeat(SeatId id) {
        return seatRepository.findById(id)
                .orElseThrow(() ->
                        SeatExceptions.notFound(id)
                );
    }

    private void applyUpdate(Seat seat, UpdateSeatRequest request) {
        seat.update(
                request.getRowLabel(),
                request.getColumnNumber(),
                request.getSeatTypeId(),
                request.getSeatStatus()
        );
    }

    private Seat persist(Seat seat) {
        try {
            return seatRepository.update(seat);
        } catch (OptimisticLockException | ObjectOptimisticLockingFailureException ex) {
            throw CommonExceptions.concurrencyConflict();
        }
    }
}
