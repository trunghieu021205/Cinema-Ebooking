package com.cinemaebooking.backend.movie.infrastructure.persistence.repository;

import com.cinemaebooking.backend.infrastructure.persistence.repository.SoftDeleteJpaRepository;
import com.cinemaebooking.backend.movie.infrastructure.persistence.entity.GenreJpaEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface GenreJpaRepository extends SoftDeleteJpaRepository<GenreJpaEntity> {

    @Query("SELECT CASE WHEN COUNT(g) > 0 THEN true ELSE false END FROM GenreJpaEntity g WHERE g.name = :name AND g.deletedAt IS NULL")
    boolean existsByName(@Param("name") String name);

    @Query("SELECT CASE WHEN COUNT(g) > 0 THEN true ELSE false END FROM GenreJpaEntity g WHERE g.name = :name AND g.id != :id AND g.deletedAt IS NULL")
    boolean existsByNameAndIdNot(@Param("name") String name, @Param("id") Long id);

    Set<GenreJpaEntity> findAllByIdIn(@Param("ids") Set<Long> ids);
}