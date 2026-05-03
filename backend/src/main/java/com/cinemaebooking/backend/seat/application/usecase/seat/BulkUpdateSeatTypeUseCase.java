package com.cinemaebooking.backend.seat.application.usecase.seat;

import com.cinemaebooking.backend.common.exception.ErrorCategory;
import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.seat.application.dto.seat.BulkUpdateResponse;
import com.cinemaebooking.backend.seat.application.port.seatType.SeatTypeRepository;
import com.cinemaebooking.backend.seat.domain.model.seat.Seat;
import com.cinemaebooking.backend.seat.domain.valueObject.seat.SeatId;
import com.cinemaebooking.backend.seat.domain.valueObject.seatType.SeatTypeId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class BulkUpdateSeatTypeUseCase {

    private final BulkSeatUpdateHelper helper;
    private final SeatTypeRepository seatTypeRepository;

    public BulkUpdateResponse execute(List<SeatId> seatIds, SeatTypeId newTypeId) {
        if (seatIds == null || seatIds.isEmpty()) {
            throw CommonExceptions.invalidInput("seatIds", ErrorCategory.REQUIRED, "seatIds must not be empty");
        }
        if (newTypeId == null) {
            throw CommonExceptions.invalidInput("seatTypeId", ErrorCategory.REQUIRED, "seatTypeId must not be null");
        }
        if (!seatTypeRepository.existsById(newTypeId)) {
            throw CommonExceptions.invalidInput("seatTypeId", ErrorCategory.INVALID_VALUE, "SeatType not found");
        }

        // Action cập nhật loại ghế
        Consumer<Seat> updateAction = seat -> seat.setSeatTypeId(newTypeId.getValue());
        return helper.updateSeats(seatIds, updateAction);
    }
}