package com.cinemaebooking.backend.seat.application.usecase.seat;

import com.cinemaebooking.backend.seat.application.port.seat.SeatRepository;
import com.cinemaebooking.backend.seat.application.port.seatType.SeatTypeRepository;
import com.cinemaebooking.backend.seat.domain.model.seat.Seat;
import com.cinemaebooking.backend.seat.domain.valueObject.seat.SeatId;
import com.cinemaebooking.backend.seat.domain.valueObject.seatType.SeatTypeId;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BulkUpdateSeatTypeUseCase {

    private final SeatRepository seatRepository;
    private final SeatTypeRepository seatTypeRepository;

    @Transactional
    public void execute(List<SeatId> seatIds, SeatTypeId seatTypeId) {

        if (seatIds == null || seatIds.isEmpty()) {
            throw new IllegalArgumentException("SeatIds must not be empty");
        }

        if (!seatTypeRepository.existsById(seatTypeId)) {
            throw new RuntimeException("SeatType not found");
        }

        List<Seat> seats = seatRepository.findAllById(seatIds);

        for (Seat seat : seats) {
            seat.setSeatTypeId(seatTypeId.getValue());
        }

        seatRepository.updateBatch(seats);
    }
}