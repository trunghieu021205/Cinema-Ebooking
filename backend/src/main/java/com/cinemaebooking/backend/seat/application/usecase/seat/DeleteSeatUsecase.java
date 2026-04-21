package com.cinemaebooking.backend.seat.application.usecase.seat;

import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.common.exception.domain.SeatExceptions;
import com.cinemaebooking.backend.seat.application.port.seat.SeatRepository;
import com.cinemaebooking.backend.seat.domain.valueObject.seat.SeatId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteSeatUsecase {

    private final SeatRepository seatRepository;

    public void execute(SeatId id) {

        // ================== INPUT VALIDATION ==================
        if (id == null) {
            throw CommonExceptions.invalidInput("Seat id must not be null");
        }

        // ================== BUSINESS VALIDATION ==================
        if (!seatRepository.existsById(id)) {
            throw SeatExceptions.notFound(id);
        }

        // ================== DELETE ==================
        seatRepository.deleteById(id);
    }
}