package com.cinemaebooking.backend.movie.infrastructure.mapper.movie;

import com.cinemaebooking.backend.infrastructure.mapper.BaseMapper;
import com.cinemaebooking.backend.movie.domain.model.Movie;
import com.cinemaebooking.backend.movie.infrastructure.persistence.entity.MovieJpaEntity;

public interface MovieMapper extends BaseMapper<Movie, MovieJpaEntity> {
    void updateEntity(MovieJpaEntity entity, Movie domain);
}