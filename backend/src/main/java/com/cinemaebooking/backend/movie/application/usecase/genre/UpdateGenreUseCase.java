package com.cinemaebooking.backend.movie.application.usecase.genre;

import com.cinemaebooking.backend.movie.application.dto.genre.GenreResponse;
import com.cinemaebooking.backend.movie.application.dto.genre.UpdateGenreRequest;
import com.cinemaebooking.backend.movie.application.mapper.GenreResponseMapper;
import com.cinemaebooking.backend.movie.application.port.GenreRepository;
import com.cinemaebooking.backend.movie.application.validator.GenreCommandValidator;
import com.cinemaebooking.backend.movie.domain.model.Genre;
import com.cinemaebooking.backend.movie.domain.valueobject.GenreId;
import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.common.exception.domain.GenreExceptions;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateGenreUseCase {

    private final GenreRepository genreRepository;
    private final GenreResponseMapper mapper;
    private final GenreCommandValidator validator;

    public GenreResponse execute(GenreId id, UpdateGenreRequest request) {
        validator.validateUpdateRequest(id, request);

        Genre genre = loadGenre(id);
        genre.update(request.getName());

        Genre saved = persist(genre);
        return mapper.toResponse(saved);
    }

    private Genre loadGenre(GenreId id) {
        return genreRepository.findById(id)
                .orElseThrow(() -> GenreExceptions.notFound(id));
    }

    private Genre persist(Genre genre) {
        try {
            return genreRepository.update(genre);
        } catch (OptimisticLockException | ObjectOptimisticLockingFailureException ex) {
            throw CommonExceptions.concurrencyConflict();
        }
    }
}