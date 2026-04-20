package com.cinemaebooking.backend.seat.infrastructure.persistence.repository;

import com.cinemaebooking.backend.infrastructure.persistence.repository.SoftDeleteJpaRepository;
import com.cinemaebooking.backend.seat.infrastructure.persistence.entity.SeatTypeJpaEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatTypeJpaRepository extends SoftDeleteJpaRepository<SeatTypeJpaEntity> {
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Long id);
}
