package com.cinemaebooking.backend.movie.infrastructure.mapper;

import com.cinemaebooking.backend.movie.domain.model.Genre;
import com.cinemaebooking.backend.movie.domain.valueobject.GenreId;
import com.cinemaebooking.backend.movie.infrastructure.persistence.entity.GenreJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class GenreMapperImpl implements GenreMapper {

    @Override
    public GenreJpaEntity toEntity(Genre domain) {
        if (domain == null) return null;
        return GenreJpaEntity.builder()
                .id(domain.getId() != null ? domain.getId().getValue() : null)
                .name(domain.getName())
                .build();
    }

    @Override
    public Genre toDomain(GenreJpaEntity entity) {
        if (entity == null) return null;
        return Genre.builder()
                .id(GenreId.ofNullable(entity.getId()))
                .name(entity.getName())
                .build();
    }

    @Override
    public void updateEntity(GenreJpaEntity entity, Genre domain) {
        if (entity == null || domain == null) return;
        entity.setName(domain.getName());
    }
}