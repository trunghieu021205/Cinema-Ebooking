package com.cinemaebooking.backend.seat.application.usecase.seat;

import com.cinemaebooking.backend.seat.application.port.seat.SeatRepository;
import com.cinemaebooking.backend.seat.domain.model.seat.Seat;
import com.cinemaebooking.backend.seat.domain.valueObject.seat.SeatId;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BulkInactiveSeatUseCase {

    private final SeatRepository seatRepository;

    @Transactional
    public void execute(List<SeatId> seatIds) {

        if (seatIds == null || seatIds.isEmpty()) {
            throw new IllegalArgumentException("SeatIds must not be empty");
        }

        List<Seat> seats = seatRepository.findAllById(seatIds);

        for (Seat seat : seats) {
            seat.markInactive();
        }

        seatRepository.updateBatch(seats);
    }
}
