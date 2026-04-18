package com.cinemaebooking.backend.seat.infrastructure.persistence.repository;

import com.cinemaebooking.backend.seat.infrastructure.persistence.entity.SeatJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatJpaRepository extends JpaRepository<SeatJpaEntity, Long> {

    boolean existsByRoom_IdAndRowLabelAndColumnNumber(
            Long roomId,
            String rowLabel,
            Integer columnNumber
    );

    Page<SeatJpaEntity> findByRoom_Id(Long roomId, Pageable pageable);

    boolean existsByRoom_IdAndRowLabelAndColumnNumberAndIdNot(
            Long roomId,
            String rowLabel,
            Integer columnNumber,
            Long id
    );
}
