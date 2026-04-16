package com.cinemaebooking.backend.cinema.application.port;

import com.cinemaebooking.backend.cinema.domain.model.Cinema;
import com.cinemaebooking.backend.cinema.domain.valueobject.CinemaId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * CinemaRepository - Domain Port for Cinema persistence operations.
 * Responsibility:
 * - Define persistence contract for Cinema
 * - Completely independent of infrastructure (JPA, DB)
 * Rule:
 * - Work with Domain objects only
 * - No JpaEntity usage
 *
 * @author Hieu Nguyen
 * @since 2026
 */
public interface CinemaRepository {

    Cinema create(Cinema cinema);

    Cinema update(Cinema cinema);

    Optional<Cinema> findById(CinemaId id);

    Page<Cinema> findAll(Pageable pageable);

    void deleteById(CinemaId id);

    boolean existsById(CinemaId id);

    boolean existsByName(String name);

    Optional<Cinema> findByName(String name);
}