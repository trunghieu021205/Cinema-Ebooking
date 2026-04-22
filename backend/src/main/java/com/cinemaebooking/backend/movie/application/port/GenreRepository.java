package com.cinemaebooking.backend.movie.application.port;

import com.cinemaebooking.backend.movie.domain.model.Genre;
import com.cinemaebooking.backend.movie.domain.valueobject.GenreId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.Set;

public interface GenreRepository {
    Genre create(Genre genre);
    Genre update(Genre genre);
    Optional<Genre> findById(GenreId id);
    Set<Genre> findAllByIds(Set<GenreId> ids);
    Page<Genre> findAll(Pageable pageable);
    void deleteById(GenreId id);
    boolean existsById(GenreId id);
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, GenreId id);
}