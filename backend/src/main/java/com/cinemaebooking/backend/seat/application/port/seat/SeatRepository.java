package com.cinemaebooking.backend.seat.application.port.seat;

import com.cinemaebooking.backend.seat.domain.model.seat.Seat;
import com.cinemaebooking.backend.seat.domain.valueObject.seat.SeatId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface SeatRepository {

    Seat create(Seat seat);
    Seat update(Seat seat);
    Optional<Seat> findById(SeatId id);
    Page<Seat> findAll(Pageable pageable);
    void deleteById(SeatId id);
    Page<Seat> findByRoomId(Long roomId, Pageable pageable);
    boolean existsByRoomIdAndRowLabelAndColumnNumber(
            Long roomId,
            String rowLabel,
            Integer columnNumber
    );

    boolean existsByRoomIdAndRowLabelAndColumnNumberAndIdNot(
            Long roomId,
            String rowLabel,
            Integer columnNumber,
            Long id
    );
}

