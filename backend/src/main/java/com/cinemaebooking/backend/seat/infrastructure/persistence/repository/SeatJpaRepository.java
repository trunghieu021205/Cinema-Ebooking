package com.cinemaebooking.backend.seat.infrastructure.persistence.repository;

import com.cinemaebooking.backend.infrastructure.persistence.repository.SoftDeleteJpaRepository;
import com.cinemaebooking.backend.seat.infrastructure.persistence.entity.SeatJpaEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatJpaRepository extends SoftDeleteJpaRepository<SeatJpaEntity> {

    boolean existsByRoom_IdAndRowLabelAndColumnNumber(
            Long roomId,
            String rowLabel,
            Integer columnNumber
    );

    List<SeatJpaEntity> findByRoom_Id(Long roomId);

    boolean existsByRoom_IdAndRowLabelAndColumnNumberAndIdNot(
            Long roomId,
            String rowLabel,
            Integer columnNumber,
            Long id
    );

    List<SeatJpaEntity> findAllById(Iterable<Long> ids);
}