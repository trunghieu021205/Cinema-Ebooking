package com.cinemaebooking.backend.seat.application.port.seat;

import com.cinemaebooking.backend.seat.domain.model.seat.Seat;
import com.cinemaebooking.backend.seat.domain.valueObject.seat.SeatId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface SeatRepository {

    Seat create(Seat seat);
    Seat update(Seat seat);
    Optional<Seat> findById(SeatId id);
    Page<Seat> findAll(Pageable pageable);
    List<Seat> findAllByIds(List<Long> ids);
    void deleteById(SeatId id);
    boolean existsById(SeatId id);
    List<Seat> findByRoomId(Long roomId);
    boolean existsByRoomIdAndRowLabelAndColumnNumber(
            Long roomId,
            String rowLabel,
            Integer columnNumber
    );

    boolean existsByRoomIdAndRowLabelAndColumnNumberAndIdNot(
            Long roomId,
            String rowLabel,
            Integer columnNumber,
            SeatId id
    );

}





