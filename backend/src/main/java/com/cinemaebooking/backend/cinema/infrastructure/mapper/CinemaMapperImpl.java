package com.cinemaebooking.backend.cinema.infrastructure.mapper;

import com.cinemaebooking.backend.cinema.domain.model.Cinema;
import com.cinemaebooking.backend.cinema.domain.valueobject.CinemaId;
import com.cinemaebooking.backend.cinema.infrastructure.persistence.entity.CinemaJpaEntity;
import org.springframework.stereotype.Component;

/**
 * CinemaMapperImpl - Implementation of CinemaMapper.
 * Responsibility:
 * - Convert Domain ↔ JpaEntity
 * - Handle ID conversion (CinemaId ↔ Long)
 * - Keep mapping logic simple and isolated
 *
 * @author Hieu Nguyen
 * @since 2026
 */
@Component
public class CinemaMapperImpl implements CinemaMapper {

    @Override
    public CinemaJpaEntity toEntity(Cinema domain) {
        if (domain == null) return null;

        return CinemaJpaEntity.builder()
                .id(domain.getId() != null ? domain.getId().getValue() : null)
                .name(domain.getName())
                .address(domain.getAddress())
                .city(domain.getCity())
                .status(domain.getStatus())
                .build();
    }

    @Override
    public Cinema toDomain(CinemaJpaEntity entity) {
        if (entity == null) return null;

        return Cinema.builder()
                .id(CinemaId.ofNullable(entity.getId()))
                .name(entity.getName())
                .address(entity.getAddress())
                .city(entity.getCity())
                .status(entity.getStatus())
                .build();
    }

    @Override
    public void updateEntity(CinemaJpaEntity entity, Cinema domain) {
        if (entity == null || domain == null) return;

        entity.setName(domain.getName());
        entity.setAddress(domain.getAddress());
        entity.setCity(domain.getCity());
        entity.setStatus(domain.getStatus());
    }
}