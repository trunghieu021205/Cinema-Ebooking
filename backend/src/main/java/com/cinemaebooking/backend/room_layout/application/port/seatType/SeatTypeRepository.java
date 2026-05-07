package com.cinemaebooking.backend.room_layout.application.port.seatType;

import com.cinemaebooking.backend.room_layout.domain.model.seatType.SeatType;
import com.cinemaebooking.backend.room_layout.domain.valueObject.seatType.SeatTypeId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
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

    Optional<SeatType> findByNameIgnoreCase(String name);

    Optional<BigDecimal> findBasePriceById(SeatTypeId seatTypeId);
}
