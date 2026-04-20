package com.cinemaebooking.backend.common.exception.domain;

import com.cinemaebooking.backend.movie.domain.valueobject.MovieId;
import com.cinemaebooking.backend.common.exception.BaseException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MovieExceptions {

    public static BaseException notFound(MovieId id) {
        return CommonExceptions.resourceNotFound(
                "Movie not found with id: " + id
        );
    }

    public static BaseException duplicateMovieTitle(String title) {
        return CommonExceptions.resourceAlreadyExists(
                "Movie title already exists: " + title
        );
    }

}