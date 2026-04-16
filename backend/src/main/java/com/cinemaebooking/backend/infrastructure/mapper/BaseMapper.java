package com.cinemaebooking.backend.infrastructure.mapper;

/**
 * BaseMapper - Contract for Domain ↔ JpaEntity mapping.
 * RULES:
 * - MUST NOT contain business logic
 * - MUST be stateless
 * - MUST NOT call database
 *
 * @param <D> Domain model
 * @param <E> JPA entity
 *
 * @author Hieu Nguyen
 * @since 2026
 */
public interface BaseMapper<D, E> {

    E toEntity(D domain);

    D toDomain(E entity);
}