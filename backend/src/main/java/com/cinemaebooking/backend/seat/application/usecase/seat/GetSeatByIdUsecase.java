package com.cinemaebooking.backend.seat.application.usecase.seat;

import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.common.exception.domain.SeatExceptions;
import com.cinemaebooking.backend.seat.application.dto.seat.SeatResponse;
import com.cinemaebooking.backend.seat.application.mapper.seat.SeatResponseMapper;
import com.cinemaebooking.backend.seat.application.port.seat.SeatRepository;
import com.cinemaebooking.backend.seat.domain.valueObject.seat.SeatId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetSeatByIdUsecase {

    private final SeatRepository seatRepository;
    private final SeatResponseMapper mapper;

    public SeatResponse execute(SeatId id) {
        if (id == null) {
            throw CommonExceptions.invalidInput("Seat id must not be null");
        }
        return seatRepository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> SeatExceptions.notFound(id));
    }
}
