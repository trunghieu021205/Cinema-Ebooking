package com.cinemaebooking.backend.common.mapper;
/**
 * Base mapper for converting between Domain and JpaEntity
 *
 * @param <D> Domain
 * @param <E> Jpa Entity
 * @author Hieu Nguyen
 * @since 2026
 */
public interface BaseMapper<D, E> {

    /**
     * Convert Domain → JpaEntity
     */
    E toEntity(D domain);

    /**
     * Convert JpaEntity → Domain
     */
    D toDomain(E entity);
}
