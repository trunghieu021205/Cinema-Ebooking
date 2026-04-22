package com.cinemaebooking.backend.movie.infrastructure.persistence.repository;

import com.cinemaebooking.backend.infrastructure.persistence.repository.SoftDeleteJpaRepository;
import com.cinemaebooking.backend.movie.infrastructure.persistence.entity.MovieJpaEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MovieJpaRepository extends SoftDeleteJpaRepository<MovieJpaEntity> {

    boolean existsByTitle(String title);

    boolean existsByTitleAndIdNot(String title, Long id);

    Optional<MovieJpaEntity> findByTitleIgnoreCase(String title);
}