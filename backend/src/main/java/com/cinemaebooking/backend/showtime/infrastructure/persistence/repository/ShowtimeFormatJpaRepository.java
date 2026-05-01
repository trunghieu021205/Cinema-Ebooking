package com.cinemaebooking.backend.showtime.infrastructure.persistence.repository;

import com.cinemaebooking.backend.infrastructure.persistence.repository.SoftDeleteJpaRepository;
import com.cinemaebooking.backend.showtime.infrastructure.persistence.entity.ShowtimeFormatJpaEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowtimeFormatJpaRepository extends SoftDeleteJpaRepository<ShowtimeFormatJpaEntity> {

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);
}