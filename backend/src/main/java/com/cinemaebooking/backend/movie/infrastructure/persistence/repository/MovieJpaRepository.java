package com.cinemaebooking.backend.movie.infrastructure.persistence.repository;

import com.cinemaebooking.backend.infrastructure.persistence.repository.SoftDeleteJpaRepository;
import com.cinemaebooking.backend.movie.infrastructure.persistence.entity.MovieJpaEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MovieJpaRepository extends SoftDeleteJpaRepository<MovieJpaEntity> {

    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END FROM MovieJpaEntity m WHERE m.title = :title AND m.deletedAt IS NULL")
    boolean existsByTitle(@Param("title") String title);

    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END FROM MovieJpaEntity m WHERE m.title = :title AND m.id != :id AND m.deletedAt IS NULL")
    boolean existsByTitleAndIdNot(@Param("title") String title, @Param("id") Long id);

    Optional<MovieJpaEntity> findByTitleIgnoreCase(@Param("title") String title);
}