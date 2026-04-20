package com.cinemaebooking.backend.cinema.infrastructure.adapter;

import com.cinemaebooking.backend.cinema.application.port.CinemaRepository;
import com.cinemaebooking.backend.cinema.domain.model.Cinema;
import com.cinemaebooking.backend.cinema.domain.valueobject.CinemaId;
import com.cinemaebooking.backend.cinema.infrastructure.mapper.CinemaMapper;
import com.cinemaebooking.backend.cinema.infrastructure.persistence.entity.CinemaJpaEntity;
import com.cinemaebooking.backend.cinema.infrastructure.persistence.repository.CinemaJpaRepository;
import com.cinemaebooking.backend.common.exception.domain.CinemaExceptions;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * CinemaRepositoryImpl - Adapter implementation of CinemaRepository (Port).
 * Responsibility:
 * - Bridge between Domain and Persistence layer
 * - Use JPA for database interaction
 * - Convert Domain ↔ JpaEntity via Mapper
 * Rule:
 * - No business logic
 * - No JpaEntity leakage outside
 * - Always map through mapper
 *
 * @author Hieu Nguyen
 * @since 2026
 */
@Repository
@RequiredArgsConstructor
public class CinemaRepositoryImpl implements CinemaRepository {

    private final CinemaJpaRepository jpaRepository;
    private final CinemaMapper mapper;

    @Override
    public Cinema create(Cinema cinema) {
        return mapper.toDomain(
                jpaRepository.save(mapper.toEntity(cinema))
        );
    }

    @Override
    public Cinema update(Cinema cinema) {
        CinemaJpaEntity entity = jpaRepository.findByIdOrThrow(cinema.getId().getValue());

        mapper.updateEntity(entity, cinema);

        return mapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public Optional<Cinema> findById(CinemaId id) {
        return jpaRepository.findById(id.getValue())
                .map(mapper::toDomain);
    }

    @Override
    public Page<Cinema> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable)
                .map(mapper::toDomain);
    }

    @Override
    public void deleteById(CinemaId id) {
        CinemaJpaEntity cinema = jpaRepository.findByIdOrThrow(id.getValue());
        jpaRepository.delete(cinema);
    }

    @Override
    public boolean existsById(CinemaId id) {
        return jpaRepository.existsById(id.getValue());
    }

    @Override
    public boolean existsByName(String name) {
        return jpaRepository.existsByName(name);
    }

    @Override

    public boolean existsByNameAndIdNot(String name, CinemaId id) {
        return jpaRepository.existsByNameAndIdNot(name, id.getValue());
    }

    @Override
    public Optional<Cinema> findByNameIgnoreCase(String name) {
        return jpaRepository.findByNameIgnoreCase(name)
                .map(mapper::toDomain);
    }

    @Override
    public boolean existsByAddressAndCity(String address, String city) {
        return jpaRepository.existsByAddressAndCity(address, city);
    }

    @Override
    public boolean existsByAddressAndCityAndIdNot(String address, String city, CinemaId id) {
        return jpaRepository.existsByAddressAndCityAndIdNot(address, city, id.getValue());
    }
}

