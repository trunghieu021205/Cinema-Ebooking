package com.cinemaebooking.backend.common.exception.domain;

import com.cinemaebooking.backend.common.exception.BaseException;
import com.cinemaebooking.backend.common.exception.ErrorCode;
import com.cinemaebooking.backend.movie.domain.valueobject.MovieId;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MovieExceptions {

    public static BaseException notFound(MovieId id) {
        return new BaseException(ErrorCode.MOVIE_NOT_FOUND,
                "Movie not found with id: " + id);
    }

}