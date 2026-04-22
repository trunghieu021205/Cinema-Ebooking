package com.cinemaebooking.backend.movie.application.mapper;

import com.cinemaebooking.backend.movie.application.dto.genre.GenreResponse;
import com.cinemaebooking.backend.movie.domain.model.Genre;
import org.springframework.stereotype.Component;

@Component
public class GenreResponseMapper {

    public GenreResponse toResponse(Genre genre) {
        if (genre == null) return null;
        return new GenreResponse(
                genre.getId() != null ? genre.getId().getValue() : null,
                genre.getName()
        );
    }
}