package com.cinemaebooking.backend.seat.application.usecase.seatType;

import com.cinemaebooking.backend.seat.application.dto.seatType.SeatTypeResponse;
import com.cinemaebooking.backend.seat.application.mapper.seatType.SeatTypeResponseMapper;
import com.cinemaebooking.backend.seat.application.port.seatType.SeatTypeRepository;
import com.cinemaebooking.backend.seat.domain.valueObject.seatType.SeatTypeId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetSeatTypeByIdUsecase {

    private final SeatTypeRepository repository;
    private final SeatTypeResponseMapper mapper;

    public SeatTypeResponse execute(Long id) {
        return repository.findById(new SeatTypeId(id))
                .map(mapper::toResponse)
                .orElseThrow(() -> new RuntimeException("SeatType not found"));
    }
}
