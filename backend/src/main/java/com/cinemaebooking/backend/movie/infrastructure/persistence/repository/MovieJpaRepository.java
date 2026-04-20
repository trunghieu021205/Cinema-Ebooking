package com.cinemaebooking.backend.movie.infrastructure.persistence.repository;

import com.cinemaebooking.backend.infrastructure.persistence.repository.SoftDeleteJpaRepository;
import com.cinemaebooking.backend.movie.infrastructure.persistence.entity.MovieJpaEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface MovieJpaRepository extends SoftDeleteJpaRepository<MovieJpaEntity> {

    boolean existsByTitle(String title);

    boolean existsByTitleAndIdNot(String title, Long id);

    /**
     * Find movie by ID and eagerly fetch genres to avoid LazyInitializationException.
     */
    @EntityGraph(attributePaths = {"genres"})
    Optional<MovieJpaEntity> findWithGenresById(Long id);
}