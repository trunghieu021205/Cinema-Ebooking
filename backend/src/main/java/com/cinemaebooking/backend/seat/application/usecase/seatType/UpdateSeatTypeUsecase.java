package com.cinemaebooking.backend.seat.application.usecase.seatType;

import com.cinemaebooking.backend.seat.application.dto.seatType.SeatTypeResponse;
import com.cinemaebooking.backend.seat.application.dto.seatType.UpdateSeatTypeRequest;
import com.cinemaebooking.backend.seat.application.mapper.seatType.SeatTypeResponseMapper;
import com.cinemaebooking.backend.seat.application.port.seatType.SeatTypeRepository;
import com.cinemaebooking.backend.seat.domain.model.seatType.SeatType;
import com.cinemaebooking.backend.seat.domain.valueObject.seatType.SeatTypeId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateSeatTypeUsecase {

    private final SeatTypeRepository repository;
    private final SeatTypeResponseMapper mapper;

    public SeatTypeResponse execute(Long id, UpdateSeatTypeRequest request) {

        SeatType seatType = SeatType.builder()
                .id(new SeatTypeId(id))
                .name(request.getName())
                .basePrice(request.getBasePrice())
                .build();

        return mapper.toResponse(repository.update(seatType));
    }
}
