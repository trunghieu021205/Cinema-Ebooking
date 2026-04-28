package com.cinemaebooking.backend.movie.application.validator;

import com.cinemaebooking.backend.movie.application.dto.genre.CreateGenreRequest;
import com.cinemaebooking.backend.movie.application.dto.genre.UpdateGenreRequest;
import com.cinemaebooking.backend.movie.application.port.GenreRepository;
import com.cinemaebooking.backend.movie.domain.valueobject.GenreId;
import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.common.exception.domain.GenreExceptions;
import com.cinemaebooking.backend.common.validation.engine.ValidationEngine;
import com.cinemaebooking.backend.common.validation.factory.ValidationFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GenreCommandValidator {

    private final GenreRepository genreRepository;

    public void validateCreateRequest(CreateGenreRequest request) {
        if (request == null) {
            throw CommonExceptions.invalidInput("Request must not be null");
        }

        String name = normalize(request.getName());
        ValidationEngine.of()
                .validate(name, "name", ValidationFactory.genre().nameRules())
                .throwIfInvalid();

        if (genreRepository.existsByName(name)) {
            throw GenreExceptions.duplicateName(name);
        }
    }

    public void validateUpdateRequest(GenreId id, UpdateGenreRequest request) {
        if (id == null || request == null) {
            throw CommonExceptions.invalidInput("Genre id and request must not be null");
        }

        String name = normalize(request.getName());
        if (name != null) {
            ValidationEngine.of()
                    .validate(name, "name", ValidationFactory.genre().nameRules())
                    .throwIfInvalid();

            if (genreRepository.existsByNameAndIdNot(name, id)) {
                throw GenreExceptions.duplicateName(name);
            }
        }
    }

    private String normalize(String value) {
        if (value == null) return null;
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}