package com.cinemaebooking.backend.seat.application.usecase.seat;

import com.cinemaebooking.backend.seat.application.dto.seat.SeatResponse;
import com.cinemaebooking.backend.seat.application.mapper.seat.SeatResponseMapper;
import com.cinemaebooking.backend.seat.application.port.seat.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetAllSeatsUsecase {

    private final SeatRepository repository;
    private final SeatResponseMapper mapper;

    public Page<SeatResponse> execute(Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::toResponse);
    }
}
