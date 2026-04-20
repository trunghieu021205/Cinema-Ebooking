package com.cinemaebooking.backend.seat.application.usecase.seatType;

import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.common.exception.domain.SeatTypeExceptions;
import com.cinemaebooking.backend.seat.application.port.seatType.SeatTypeRepository;
import com.cinemaebooking.backend.seat.domain.valueObject.seatType.SeatTypeId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteSeatTypeUsecase {

    private final SeatTypeRepository seatTypeRepository;

    public void execute(SeatTypeId id) {

        // ================== INPUT VALIDATION ==================
        if (id == null) {
            throw CommonExceptions.invalidInput("SeatType id must not be null");
        }

        // ================== BUSINESS VALIDATION ==================
        if (!seatTypeRepository.existsById(id)) {
            throw SeatTypeExceptions.notFound(id);
        }

        // ================== DELETE ==================
        seatTypeRepository.deleteById(id);
    }
}
