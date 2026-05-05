package com.cinemaebooking.backend.movie.application.validator;

import com.cinemaebooking.backend.common.exception.ErrorCategory;
import com.cinemaebooking.backend.movie.application.dto.movie.CreateMovieRequest;
import com.cinemaebooking.backend.movie.application.dto.movie.UpdateMovieRequest;
import com.cinemaebooking.backend.movie.application.port.MovieRepository;
import com.cinemaebooking.backend.movie.domain.valueobject.MovieId;
import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.common.validation.engine.ValidationEngine;
import com.cinemaebooking.backend.common.validation.factory.ValidationFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class MovieCommandValidator {

    private final MovieRepository movieRepository;

    // ================== PUBLIC API ==================

    public void validateCreateRequest(CreateMovieRequest request) {
        validateRequest(request, null, false);
    }

    public void validateUpdateRequest(MovieId id, UpdateMovieRequest request) {
        if (id == null) {
            throw CommonExceptions.invalidInput("Movie id and request must not be null");
        }
        validateRequest(request, id, true);
    }

    // ================== CORE VALIDATION ==================

    private void validateRequest(Object requestObj, MovieId id, boolean isUpdate) {
        if (requestObj == null) {
            throw CommonExceptions.invalidInput(isUpdate
                    ? "Movie id and request must not be null"
                    : "Create movie request must not be null");
        }

        String title = extractTitle(requestObj);
        String description = extractDescription(requestObj);
        Integer duration = extractDuration(requestObj);
        LocalDate releaseDate = extractReleaseDate(requestObj);
        Set<Long> genreIds = extractGenreIds(requestObj);

        String normalizedTitle = normalize(title);
        String normalizedDescription = normalize(description);

        ValidationEngine engine = ValidationEngine.of();

        // ================== PHASE 1: FORMAT VALIDATION ==================
        // Title
        if (!isUpdate && normalizedTitle == null) {
            engine.validate(normalizedTitle, "title", ValidationFactory.movie().titleRules());
        } else if (normalizedTitle != null) {
            engine.validate(normalizedTitle, "title", ValidationFactory.movie().titleRules());
        }

        // Description
        if (!isUpdate && normalizedDescription == null) {
            engine.validate(normalizedDescription, "description", ValidationFactory.movie().descriptionRules());
        } else if (normalizedDescription != null) {
            engine.validate(normalizedDescription, "description", ValidationFactory.movie().descriptionRules());
        }

        // Duration
        if (!isUpdate && duration == null) {
            engine.validate(duration, "duration", ValidationFactory.movie().durationRules());
        } else if (duration != null) {
            engine.validate(duration, "duration", ValidationFactory.movie().durationRules());
        }

        // ReleaseDate (giả sử có rules trong profile)
        if (!isUpdate && releaseDate == null) {
            engine.validate(releaseDate, "releaseDate", ValidationFactory.movie().releaseDateRules());
        } else if (releaseDate != null) {
            engine.validate(releaseDate, "releaseDate", ValidationFactory.movie().releaseDateRules());
        }

        // GenreIds validation (không dùng rule thông thường)
        if (!isUpdate) {
            if (genreIds == null || genreIds.isEmpty()) {
                throw CommonExceptions.invalidInput("genreIds", ErrorCategory.INVALID_VALUE,"Movie must have at least one genre");
            }
        } else {
            if (genreIds != null && genreIds.isEmpty()) {
                throw CommonExceptions.invalidInput("genreIds", ErrorCategory.INVALID_VALUE,"Movie must have at least one genre");
            }
        }

        if (engine.hasErrors()) {
            engine.throwIfInvalid();
            return;
        }

        // ================== PHASE 2: BUSINESS VALIDATION ==================
        if (normalizedTitle != null) {
            if (!isUpdate) {
                engine.validateUnique(normalizedTitle, "title", movieRepository::existsByTitle);
            } else {
                engine.validateUnique(normalizedTitle, "title",
                        value -> movieRepository.existsByTitleAndIdNot(value, id));
            }
        }

        engine.throwIfInvalid();
    }

    // ================== EXTRACTORS ==================

    private String extractTitle(Object request) {
        if (request instanceof CreateMovieRequest req) return req.getTitle();
        if (request instanceof UpdateMovieRequest req) return req.getTitle();
        return null;
    }

    private String extractDescription(Object request) {
        if (request instanceof CreateMovieRequest req) return req.getDescription();
        if (request instanceof UpdateMovieRequest req) return req.getDescription();
        return null;
    }

    private Integer extractDuration(Object request) {
        if (request instanceof CreateMovieRequest req) return req.getDuration();
        if (request instanceof UpdateMovieRequest req) return req.getDuration();
        return null;
    }

    private LocalDate extractReleaseDate(Object request) {
        if (request instanceof CreateMovieRequest req) return req.getReleaseDate();
        if (request instanceof UpdateMovieRequest req) return req.getReleaseDate();
        return null;
    }

    private Set<Long> extractGenreIds(Object request) {
        if (request instanceof CreateMovieRequest req) return req.getGenreIds();
        if (request instanceof UpdateMovieRequest req) return req.getGenreIds();
        return null;
    }

    // ================== HELPER ==================

    private String normalize(String value) {
        if (value == null) return null;
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}