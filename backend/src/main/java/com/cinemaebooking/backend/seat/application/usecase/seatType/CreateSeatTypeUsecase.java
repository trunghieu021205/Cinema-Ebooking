package com.cinemaebooking.backend.seat.application.usecase.seatType;

import com.cinemaebooking.backend.seat.application.dto.seatType.CreateSeatTypeRequest;
import com.cinemaebooking.backend.seat.application.dto.seatType.SeatTypeResponse;
import com.cinemaebooking.backend.seat.application.mapper.seatType.SeatTypeResponseMapper;
import com.cinemaebooking.backend.seat.application.port.seatType.SeatTypeRepository;
import com.cinemaebooking.backend.seat.domain.model.seatType.SeatType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateSeatTypeUsecase {

    private final SeatTypeRepository repository;
    private final SeatTypeResponseMapper mapper;

    public SeatTypeResponse execute(CreateSeatTypeRequest request) {

        if (repository.existsByName(request.getName())) {
            throw new IllegalArgumentException("SeatType name already exists");
        }

        SeatType seatType = SeatType.builder()
                .name(request.getName())
                .basePrice(request.getBasePrice())
                .build();

        return mapper.toResponse(repository.create(seatType));
    }
}
