package com.cinemaebooking.backend.movie.infrastructure.persistence.repository;

import com.cinemaebooking.backend.infrastructure.persistence.repository.SoftDeleteJpaRepository;
import com.cinemaebooking.backend.movie.infrastructure.persistence.entity.GenreJpaEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface GenreJpaRepository extends SoftDeleteJpaRepository<GenreJpaEntity> {

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);

    Optional<GenreJpaEntity> findByNameIgnoreCase(String name);

    Set<GenreJpaEntity> findAllByIdIn(Set<Long> ids);
}