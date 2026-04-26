package com.cinemaebooking.backend.movie.domain.valueobject;

import com.cinemaebooking.backend.common.domain.BaseId;

public final class GenreId extends BaseId {

    private GenreId(Long value) {
        super(value);
    }

    public static GenreId of(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("GenreId must be positive");
        }
        return new GenreId(value);
    }

    public static GenreId ofNullable(Long value) {
        return value == null ? null : new GenreId(value);
    }
}