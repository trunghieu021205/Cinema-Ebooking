package com.cinemaebooking.backend.movie.application.validator;

import com.cinemaebooking.backend.movie.application.dto.movie.CreateMovieRequest;
import com.cinemaebooking.backend.movie.application.dto.movie.UpdateMovieRequest;
import com.cinemaebooking.backend.movie.application.port.MovieRepository;
import com.cinemaebooking.backend.movie.domain.valueobject.MovieId;
import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.common.exception.domain.MovieExceptions;
import com.cinemaebooking.backend.common.validation.engine.ValidationEngine;
import com.cinemaebooking.backend.common.validation.factory.ValidationFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MovieCommandValidator {

    private final MovieRepository movieRepository;

    public void validateCreateRequest(CreateMovieRequest request) {
        if (request == null) {
            throw CommonExceptions.invalidInput("Request must not be null");
        }

        validateFields(
                request.getTitle(),
                request.getDescription(),
                request.getDuration(),
                request.getReleaseDate()
        );

        if (movieRepository.existsByTitle(normalize(request.getTitle()))) {
            throw MovieExceptions.duplicateTitle(request.getTitle());
        }
    }

    public void validateUpdateRequest(MovieId id, UpdateMovieRequest request) {
        if (id == null || request == null) {
            throw CommonExceptions.invalidInput("Movie id and request must not be null");
        }

        validateFields(
                request.getTitle(),
                request.getDescription(),
                request.getDuration(),
                request.getReleaseDate()
        );

        String title = normalize(request.getTitle());
        if (title != null && movieRepository.existsByTitleAndIdNot(title, id)) {
            throw MovieExceptions.duplicateTitle(title);
        }
    }

    private void validateFields(String title, String description, Integer duration, java.time.LocalDate releaseDate) {
        var profile = ValidationFactory.movie();

        if (title != null) {
            ValidationEngine.validate(title, "Movie title", profile.titleRules());
        }
        if (description != null) {
            ValidationEngine.validate(description, "Movie description", profile.descriptionRules());
        }
        if (duration != null) {
            ValidationEngine.validate(duration.toString(), "Duration", profile.durationRules());
        }
        if (releaseDate != null) {
            // release date validation can be added if needed
        }
    }

    private String normalize(String value) {
        if (value == null) return null;
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}