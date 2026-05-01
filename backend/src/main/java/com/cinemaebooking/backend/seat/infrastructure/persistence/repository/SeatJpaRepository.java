package com.cinemaebooking.backend.seat.infrastructure.persistence.repository;

import com.cinemaebooking.backend.infrastructure.persistence.repository.SoftDeleteJpaRepository;
import com.cinemaebooking.backend.seat.domain.valueObject.seat.SeatId;
import com.cinemaebooking.backend.seat.infrastructure.persistence.entity.SeatJpaEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatJpaRepository extends SoftDeleteJpaRepository<SeatJpaEntity> {

    List<SeatJpaEntity> findByRoom_Id(Long roomId);

    boolean existsByRoomIdAndRowIndexAndColIndex(Long roomId, Integer rowIndex, Integer colIndex);

    List<SeatJpaEntity> findAllById(Iterable<Long> ids);
    boolean existsByRoomIdAndRowIndexAndColIndexAndIdNot(Long roomId, Integer rowIndex, Integer colIndex, SeatId id);
}