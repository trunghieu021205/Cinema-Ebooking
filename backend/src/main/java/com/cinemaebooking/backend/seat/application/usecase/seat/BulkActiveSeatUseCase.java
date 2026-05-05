package com.cinemaebooking.backend.seat.application.usecase.seat;

import com.cinemaebooking.backend.common.exception.ErrorCategory;
import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.seat.application.dto.seat.BulkUpdateResponse;
import com.cinemaebooking.backend.seat.domain.model.seat.Seat;
import com.cinemaebooking.backend.seat.domain.valueObject.seat.SeatId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@RequiredArgsConstructor
public class BulkActiveSeatUseCase {

    private final BulkSeatUpdateHelper helper;

    public BulkUpdateResponse execute(List<SeatId> seatIds) {
        if (seatIds == null || seatIds.isEmpty()) {
            throw CommonExceptions.invalidInput("seatIds", ErrorCategory.REQUIRED, "seatIds must not be empty");
        }
        return helper.updateSeats(seatIds, Seat::markActive);
    }
}