package com.cinemaebooking.backend.movie.application.usecase;

import com.cinemaebooking.backend.movie.application.dto.GenreResponse;
import com.cinemaebooking.backend.movie.application.dto.UpdateGenreRequest;
import com.cinemaebooking.backend.movie.application.mapper.GenreResponseMapper;
import com.cinemaebooking.backend.movie.application.port.GenreRepository;
import com.cinemaebooking.backend.movie.application.validator.GenreCommandValidator;
import com.cinemaebooking.backend.movie.domain.model.Genre;
import com.cinemaebooking.backend.movie.domain.valueobject.GenreId;
import com.cinemaebooking.backend.common.exception.domain.GenreExceptions;
import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateGenreUseCase {

    private final GenreRepository genreRepository;
    private final GenreResponseMapper responseMapper;
    private final GenreCommandValidator validator;

    public GenreResponse execute(GenreId id, UpdateGenreRequest request) {
        validator.validateUpdateRequest(id, request);

        Genre genre = loadGenre(id);
        applyUpdate(genre, request);
        Genre saved = persist(genre);
        return responseMapper.toResponse(saved);
    }

    private Genre loadGenre(GenreId id) {
        return genreRepository.findById(id)
                .orElseThrow(() -> GenreExceptions.notFound(id));
    }

    private void applyUpdate(Genre genre, UpdateGenreRequest request) {
        genre.updateName(request.getName());
    }

    private Genre persist(Genre genre) {
        try {
            return genreRepository.update(genre);
        } catch (OptimisticLockException | ObjectOptimisticLockingFailureException ex) {
            throw CommonExceptions.concurrencyConflict();
        }
    }
}