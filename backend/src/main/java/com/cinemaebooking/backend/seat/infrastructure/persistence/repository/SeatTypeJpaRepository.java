package com.cinemaebooking.backend.seat.infrastructure.persistence.repository;

import com.cinemaebooking.backend.seat.infrastructure.persistence.entity.SeatTypeJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatTypeJpaRepository extends JpaRepository<SeatTypeJpaEntity, Long> {
    boolean existsByName(String name);
}
