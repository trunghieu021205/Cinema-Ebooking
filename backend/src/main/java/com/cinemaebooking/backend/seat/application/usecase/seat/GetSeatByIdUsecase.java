package com.cinemaebooking.backend.seat.application.usecase.seat;

import com.cinemaebooking.backend.seat.application.dto.seat.SeatResponse;
import com.cinemaebooking.backend.seat.application.mapper.seat.SeatResponseMapper;
import com.cinemaebooking.backend.seat.application.port.seat.SeatRepository;
import com.cinemaebooking.backend.seat.domain.valueObject.seat.SeatId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetSeatByIdUsecase {

    private final SeatRepository repository;
    private final SeatResponseMapper mapper;

    public SeatResponse execute(Long id) {
        return repository.findById(new SeatId(id))
                .map(mapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Seat not found"));
    }
}
