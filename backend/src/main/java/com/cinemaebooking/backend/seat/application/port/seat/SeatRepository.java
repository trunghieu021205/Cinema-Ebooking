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
    void deleteById(SeatId id);
    boolean existsById(SeatId id);
    List<Seat> findByRoomId(Long roomId);
    List<Seat> findAllById(List<SeatId> ids);
    boolean existsByRoomId(Long roomId);
    boolean existsByRoomIdAndRowIndexAndColIndex(Long roomId, Integer rowIndex, Integer colIndex);
    boolean existsByRoomIdAndRowIndexAndColIndexAndIdNot(Long roomId, Integer rowIndex, Integer colIndex, SeatId id);
    void createBatch(List<Seat> seats);
    void updateBatch(List<Seat> seats);
}





