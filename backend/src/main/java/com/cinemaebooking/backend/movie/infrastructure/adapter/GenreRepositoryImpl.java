package com.cinemaebooking.backend.movie.infrastructure.adapter;

import com.cinemaebooking.backend.movie.application.port.GenreRepository;
import com.cinemaebooking.backend.movie.domain.model.Genre;
import com.cinemaebooking.backend.movie.domain.valueobject.GenreId;
import com.cinemaebooking.backend.movie.infrastructure.mapper.genre.GenreMapper;
import com.cinemaebooking.backend.movie.infrastructure.persistence.entity.GenreJpaEntity;
import com.cinemaebooking.backend.movie.infrastructure.persistence.repository.GenreJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class GenreRepositoryImpl implements GenreRepository {

    private final GenreJpaRepository jpaRepository;
    private final GenreMapper mapper;

    @Override
    public Genre create(Genre genre) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(genre)));
    }

    @Override
    public Genre update(Genre genre) {
        GenreJpaEntity entity = jpaRepository.findByIdOrThrow(genre.getId().getValue());
        mapper.updateEntity(entity, genre);
        return mapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public Optional<Genre> findById(GenreId id) {
        return jpaRepository.findById(id.getValue()).map(mapper::toDomain);
    }

    @Override
    public Set<Genre> findAllByIds(Set<GenreId> ids) {
        Set<Long> idValues = ids.stream().map(GenreId::getValue).collect(Collectors.toSet());
        return jpaRepository.findAllByIdIn(idValues).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toSet());
    }

    @Override
    public Page<Genre> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable).map(mapper::toDomain);
    }

    @Override
    public void deleteById(GenreId id) {
        GenreJpaEntity entity = jpaRepository.findByIdOrThrow(id.getValue());
        jpaRepository.delete(entity);
    }

    @Override
    public boolean existsById(GenreId id) {
        return jpaRepository.existsById(id.getValue());
    }

    @Override
    public boolean existsByName(String name) {
        return jpaRepository.existsByName(name);
    }

    @Override
    public boolean existsByNameAndIdNot(String name, GenreId id) {
        return jpaRepository.existsByNameAndIdNot(name, id.getValue());
    }
}