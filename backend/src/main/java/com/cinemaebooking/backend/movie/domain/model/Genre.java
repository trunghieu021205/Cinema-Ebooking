package com.cinemaebooking.backend.movie.domain.model;

import com.cinemaebooking.backend.common.domain.BaseEntity;
import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.movie.domain.valueobject.GenreId;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class Genre extends BaseEntity<GenreId> {

    private String name;

    public void update(String name) {
        validateName(name);
        this.name = name;
    }

    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw CommonExceptions.invalidInput("Genre name must not be empty");
        }
    }
}