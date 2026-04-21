package com.cinemaebooking.backend.movie.infrastructure.mapper;

import com.cinemaebooking.backend.infrastructure.mapper.BaseMapper;
import com.cinemaebooking.backend.movie.domain.model.Genre;
import com.cinemaebooking.backend.movie.infrastructure.persistence.entity.GenreJpaEntity;

public interface GenreMapper extends BaseMapper<Genre, GenreJpaEntity> {

    void updateEntity(GenreJpaEntity entity, Genre domain);
}