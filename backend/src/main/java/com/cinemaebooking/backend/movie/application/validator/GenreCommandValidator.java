package com.cinemaebooking.backend.movie.application.validator;

import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.common.exception.domain.GenreExceptions;
import com.cinemaebooking.backend.movie.application.dto.CreateGenreRequest;
import com.cinemaebooking.backend.movie.application.dto.UpdateGenreRequest;
import com.cinemaebooking.backend.movie.application.port.GenreRepository;
import com.cinemaebooking.backend.movie.domain.valueobject.GenreId;
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
        validateName(request.getName());
        validateDuplicateNameForCreate(request.getName());
    }

    public void validateUpdateRequest(GenreId id, UpdateGenreRequest request) {
        if (id == null || request == null) {
            throw CommonExceptions.invalidInput("Genre id and request must not be null");
        }
        validateName(request.getName());
        validateDuplicateNameForUpdate(id, request.getName());
    }

    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw CommonExceptions.invalidInput("Genre name must not be empty");
        }
        if (name.length() > 100) {
            throw CommonExceptions.invalidInput("Genre name must not exceed 100 characters");
        }
    }

    private void validateDuplicateNameForCreate(String name) {
        if (genreRepository.existsByName(name)) {
            throw GenreExceptions.duplicateGenreName(name);
        }
    }

    private void validateDuplicateNameForUpdate(GenreId id, String name) {
        if (genreRepository.existsByNameAndIdNot(name, id)) {
            throw GenreExceptions.duplicateGenreName(name);
        }
    }
}