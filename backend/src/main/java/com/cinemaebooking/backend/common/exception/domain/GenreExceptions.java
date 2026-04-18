package com.cinemaebooking.backend.common.exception.domain;

import com.cinemaebooking.backend.movie.domain.valueobject.GenreId;
import com.cinemaebooking.backend.common.exception.BaseException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class GenreExceptions {

    public static BaseException notFound(GenreId id) {
        return CommonExceptions.resourceNotFound(
                "Genre not found with id: " + id
        );
    }

    public static BaseException duplicateGenreName(String name) {
        return CommonExceptions.resourceAlreadyExists(
                "Genre name already exists: " + name
        );
    }
}