package com.cinemaebooking.backend.movie.application.validator;

import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.common.exception.domain.MovieExceptions;
import com.cinemaebooking.backend.movie.application.dto.CreateMovieRequest;
import com.cinemaebooking.backend.movie.application.dto.UpdateMovieRequest;
import com.cinemaebooking.backend.movie.application.port.GenreRepository;
import com.cinemaebooking.backend.movie.application.port.MovieRepository;
import com.cinemaebooking.backend.movie.domain.valueobject.GenreId;
import com.cinemaebooking.backend.movie.domain.valueobject.MovieId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MovieCommandValidator {

    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;

    public void validateCreateRequest(CreateMovieRequest request) {
        if (request == null) {
            throw CommonExceptions.invalidInput("Request must not be null");
        }

        validateTitle(request.getTitle());
        validateDuration(request.getDuration());
        validateAgeRating(request.getAgeRating());
        validateReleaseDate(request.getReleaseDate());
        validateGenreIdsExist(request.getGenreIds());

        if (movieRepository.existsByTitle(request.getTitle())) {
            throw MovieExceptions.duplicateMovieTitle(request.getTitle());
        }
    }

    public void validateUpdateRequest(MovieId id, UpdateMovieRequest request) {
        if (id == null || request == null) {
            throw CommonExceptions.invalidInput("Movie id and request must not be null");
        }

        validateTitle(request.getTitle());
        validateDuration(request.getDuration());
        validateAgeRating(request.getAgeRating());
        validateReleaseDate(request.getReleaseDate());
        validateStatus(request.getStatus());
        validateGenreIdsExist(request.getGenreIds());

        if (movieRepository.existsByTitleAndIdNot(request.getTitle(), id)) {
            throw MovieExceptions.duplicateMovieTitle(request.getTitle());
        }
    }

    private void validateTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw CommonExceptions.invalidInput("Movie title must not be empty");
        }
        if (title.length() > 255) {
            throw CommonExceptions.invalidInput("Movie title must not exceed 255 characters");
        }
    }

    private void validateDuration(Integer duration) {
        if (duration == null || duration <= 0) {
            throw CommonExceptions.invalidInput("Duration must be positive");
        }
    }

    private void validateAgeRating(Object ageRating) {
        if (ageRating == null) {
            throw CommonExceptions.invalidInput("Age rating must not be null");
        }
    }

    private void validateReleaseDate(Object releaseDate) {
        if (releaseDate == null) {
            throw CommonExceptions.invalidInput("Release date must not be null");
        }
    }

    private void validateStatus(Object status) {
        if (status == null) {
            throw CommonExceptions.invalidInput("Movie status must not be null");
        }
    }

    private void validateGenreIdsExist(Set<Long> genreIds) {
        if (genreIds == null || genreIds.isEmpty()) return;

        Set<GenreId> ids = genreIds.stream()
                .map(GenreId::ofNullable)
                .collect(Collectors.toSet());

        Set<GenreId> existingIds = genreRepository.findAllByIdIn(ids).stream()
                .map(genre -> genre.getId())
                .collect(Collectors.toSet());

        if (existingIds.size() != ids.size()) {
            throw CommonExceptions.invalidInput("One or more genre IDs do not exist");
        }
    }
}