package com.cinemaebooking.backend.seat.application.usecase.seatType;

import com.cinemaebooking.backend.seat.application.port.seatType.SeatTypeRepository;
import com.cinemaebooking.backend.seat.domain.valueObject.seatType.SeatTypeId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteSeatTypeUsecase {

    private final SeatTypeRepository repository;

    public void execute(Long id) {
        repository.deleteById(new SeatTypeId(id));
    }
}
