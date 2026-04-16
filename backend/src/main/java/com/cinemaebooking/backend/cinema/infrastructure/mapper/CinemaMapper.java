package com.cinemaebooking.backend.cinema.infrastructure.mapper;

import com.cinemaebooking.backend.cinema.domain.model.Cinema;
import com.cinemaebooking.backend.cinema.infrastructure.persistence.entity.CinemaJpaEntity;
import com.cinemaebooking.backend.infrastructure.mapper.BaseMapper;

/**
 * CinemaMapper - Contract for mapping between Cinema domain and JPA entity.
 * Responsibility:
 * - Domain ↔ JpaEntity conversion
 * - No business logic
 * - No relation mapping (handled separately)
 *
 * @author Hieu Nguyen
 * @since 2026
 */
public interface CinemaMapper extends BaseMapper<Cinema, CinemaJpaEntity> {

    /**
     * Update existing JpaEntity from Domain (for update operations).
     */
    void updateEntity(CinemaJpaEntity entity, Cinema domain);
}