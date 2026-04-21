package com.cinemaebooking.backend.movie.application.port;

import com.cinemaebooking.backend.movie.domain.model.Movie;
import com.cinemaebooking.backend.movie.domain.valueobject.MovieId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.Set;

/**
 * MovieRepository - Domain Port for Movie persistence operations.
 *
 * @author Hieu Nguyen
 * @since 2026
 */
public interface MovieRepository {

    Movie create(Movie movie, Set<Long> genreIds);

    Movie update(Movie movie, Set<Long> genreIds);

    Optional<Movie> findById(MovieId id);

    Page<Movie> findAll(Pageable pageable);

    void deleteById(MovieId id);

    boolean existsById(MovieId id);

    boolean existsByTitle(String title);

    boolean existsByTitleAndIdNot(String title, MovieId id);
}