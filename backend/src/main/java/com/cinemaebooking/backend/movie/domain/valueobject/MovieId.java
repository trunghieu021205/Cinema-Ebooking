package com.cinemaebooking.backend.movie.domain.valueobject;

import com.cinemaebooking.backend.common.domain.BaseId;

public final class MovieId extends BaseId {

    private MovieId(Long value) {
        super(value);
    }

    public static MovieId of(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("MovieId must be positive");
        }
        return new MovieId(value);
    }

    public static MovieId ofNullable(Long value) {
        return value == null ? null : new MovieId(value);
    }
}