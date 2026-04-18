package com.cinemaebooking.backend.seat.application.usecase.seatType;

import com.cinemaebooking.backend.seat.application.dto.seatType.SeatTypeResponse;
import com.cinemaebooking.backend.seat.application.mapper.seatType.SeatTypeResponseMapper;
import com.cinemaebooking.backend.seat.application.port.seatType.SeatTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetSeatTypeUsecase {

    private final SeatTypeRepository repository;
    private final SeatTypeResponseMapper mapper;

    public Page<SeatTypeResponse> execute(Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::toResponse);
    }
}
