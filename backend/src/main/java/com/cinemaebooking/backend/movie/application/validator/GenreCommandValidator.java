package com.cinemaebooking.backend.movie.application.validator;

import com.cinemaebooking.backend.movie.application.dto.genre.CreateGenreRequest;
import com.cinemaebooking.backend.movie.application.dto.genre.UpdateGenreRequest;
import com.cinemaebooking.backend.movie.application.port.GenreRepository;
import com.cinemaebooking.backend.movie.domain.valueobject.GenreId;
import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.common.validation.engine.ValidationEngine;
import com.cinemaebooking.backend.common.validation.factory.ValidationFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GenreCommandValidator {
    private final GenreRepository genreRepository;

    // ================== PUBLIC API ==================

    public void validateCreateRequest(CreateGenreRequest request) {
        validateRequest(request, null, false);
    }

    public void validateUpdateRequest(GenreId id, UpdateGenreRequest request) {
        if (id == null) {
            throw CommonExceptions.invalidInput("Genre id and request must not be null");
        }
        validateRequest(request, id, true);
    }

    // ================== CORE VALIDATION ==================

    private void validateRequest(Object requestObj, GenreId id, boolean isUpdate) {
        if (requestObj == null) {
            throw CommonExceptions.invalidInput(isUpdate
                    ? "Genre id and request must not be null"
                    : "Create genre request must not be null");
        }

        String name = extractName(requestObj);

        // Normalize before validation
        String normalizedName = normalize(name);

        ValidationEngine engine = ValidationEngine.of();

        // ================== PHASE 1: FORMAT VALIDATION ==================
        // For create: name is mandatory
        // For update: only validate if name is provided (non-null after normalization)
        if (!isUpdate && normalizedName == null) {
            // Let the validation rule catch null (nameRules should have @NotNull)
            engine.validate(normalizedName, "name", ValidationFactory.genre().nameRules());
        } else if (normalizedName != null) {
            engine.validate(normalizedName, "name", ValidationFactory.genre().nameRules());
        }

        if (engine.hasErrors()) {
            engine.throwIfInvalid();
            return;
        }

        // ================== PHASE 2: BUSINESS VALIDATION ==================
        if (normalizedName != null) {
            if (!isUpdate) {
                engine.validateUnique(normalizedName, "name", genreRepository::existsByName);
            } else {
                engine.validateUnique(normalizedName, "name",
                        value -> genreRepository.existsByNameAndIdNot(value, id));
            }
        }

        engine.throwIfInvalid();
    }

    // ================== EXTRACTORS ==================

    private String extractName(Object request) {
        if (request instanceof CreateGenreRequest req) return req.getName();
        if (request instanceof UpdateGenreRequest req) return req.getName();
        return null;
    }

    // ================== HELPER ==================

    private String normalize(String value) {
        if (value == null) return null;
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}