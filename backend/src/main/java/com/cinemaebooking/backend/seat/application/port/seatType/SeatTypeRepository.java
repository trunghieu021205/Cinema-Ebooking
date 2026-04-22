package com.cinemaebooking.backend.seat.application.port.seatType;

import com.cinemaebooking.backend.seat.domain.model.seatType.SeatType;
import com.cinemaebooking.backend.seat.domain.valueObject.seatType.SeatTypeId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface SeatTypeRepository {

    SeatType create(SeatType seatType);

    SeatType update(SeatType seatType);

    Optional<SeatType> findById(SeatTypeId id);

    Page<SeatType> findAll(Pageable pageable);

    boolean existsById(SeatTypeId id);

    void deleteById(SeatTypeId id);

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, SeatTypeId id);
}
