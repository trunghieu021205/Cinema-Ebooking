package com.cinemaebooking.backend.movie.application.usecase;

import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.common.exception.domain.MovieExceptions;
import com.cinemaebooking.backend.movie.application.dto.MovieResponse;
import com.cinemaebooking.backend.movie.application.dto.UpdateMovieRequest;
import com.cinemaebooking.backend.movie.application.mapper.MovieResponseMapper;
import com.cinemaebooking.backend.movie.application.port.MovieRepository;
import com.cinemaebooking.backend.movie.application.validator.MovieCommandValidator;
import com.cinemaebooking.backend.movie.domain.model.Movie;
import com.cinemaebooking.backend.movie.domain.valueobject.MovieId;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * UpdateMovieUseCase - Handles updating an existing Movie.
 *
 * @author Hieu Nguyen
 * @since 2026
 */
@Service
@RequiredArgsConstructor
public class UpdateMovieUseCase {

    private final MovieRepository movieRepository;
    private final MovieResponseMapper responseMapper;
    private final MovieCommandValidator validator;

    public MovieResponse execute(MovieId id, UpdateMovieRequest request) {
        validator.validateUpdateRequest(id, request);

        Movie movie = loadMovie(id);
        applyUpdate(movie, request);
        Movie saved = persist(movie, request.getGenreIds());
        return responseMapper.toResponse(saved);
    }

    private Movie loadMovie(MovieId id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> MovieExceptions.notFound(id));
    }

    private void applyUpdate(Movie movie, UpdateMovieRequest request) {
        movie.updateInfo(
                request.getTitle(),
                request.getDescription(),
                request.getDuration(),
                request.getAgeRating(),
                request.getReleaseDate(),
                request.getPosterUrl(),
                request.getBannerUrl(),
                request.getDirector(),
                request.getActors()
        );
        movie.changeStatus(request.getStatus());
    }

    private Movie persist(Movie movie, Set<Long> genreIds) {
        try {
            return movieRepository.update(movie, genreIds);
        } catch (OptimisticLockException | ObjectOptimisticLockingFailureException ex) {
            throw CommonExceptions.concurrencyConflict();
        }
    }
}