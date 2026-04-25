package com.cinemaebooking.backend.common.exception.domain;

import com.cinemaebooking.backend.common.exception.BaseException;
import com.cinemaebooking.backend.common.exception.ErrorCode;
import com.cinemaebooking.backend.movie.domain.valueobject.GenreId;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class GenreExceptions {

    public static BaseException notFound(GenreId id) {
        return new BaseException(ErrorCode.GENRE_NOT_FOUND,
                "Genre not found with id: " + id);
    }

    public static BaseException notFound(Set<Long> ids) {
        return new BaseException(ErrorCode.GENRE_NOT_FOUND,
                "Genres not found with ids: " + ids);
    }

    public static BaseException duplicateName(String name) {

        return new BaseException(ErrorCode.GENRE_ALREADY_EXISTS,
                "Genre already exists with name: " + name);
    }
}