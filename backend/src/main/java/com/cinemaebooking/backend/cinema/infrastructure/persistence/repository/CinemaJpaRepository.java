package com.cinemaebooking.backend.cinema.infrastructure.persistence.repository;

import com.cinemaebooking.backend.infrastructure.persistence.repository.BaseJpaRepository;
import com.cinemaebooking.backend.cinema.infrastructure.persistence.entity.CinemaJpaEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * CinemaJpaRepository - JPA repository for Cinema entity.
 *
 * @author Hieu Nguyen
 * @since 2026
 */
@Repository
public interface CinemaJpaRepository extends BaseJpaRepository<CinemaJpaEntity> {

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long value);

    Optional<CinemaJpaEntity> findByNameIgnoreCase(String name);

    boolean existsByAddressAndCity(String address, String city);

    boolean existsByAddressAndCityAndIdNot(String address, String city, Long id);
}