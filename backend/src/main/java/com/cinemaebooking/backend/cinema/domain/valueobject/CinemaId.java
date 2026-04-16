package com.cinemaebooking.backend.cinema.domain.valueobject;

import com.cinemaebooking.backend.common.domain.BaseId;

/**
 * CinemaId - Type-safe identifier for Cinema aggregate.
 *
 * @author Hieu Nguyen
 * @since 2026
 */
public final class CinemaId extends BaseId {

    private CinemaId(Long value) {
        super(value);
    }

    /**
     * Factory method - strict version
     */
    public static CinemaId of(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("CinemaId value must be positive number");
        }
        return new CinemaId(value);
    }

    /**
     * For mapping purpose when id may be null (e.g. new entity)
     */
    public static CinemaId ofNullable(Long value) {
        return value == null ? null : new CinemaId(value);
    }
}