package com.cinemaebooking.backend.movie.application.usecase.genre;

import com.cinemaebooking.backend.movie.application.port.GenreRepository;
import com.cinemaebooking.backend.movie.domain.valueobject.GenreId;
import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.common.exception.domain.GenreExceptions;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteGenreUseCase {

    private final GenreRepository genreRepository;

    public void execute(GenreId id) {
        if (id == null) {
            throw CommonExceptions.invalidInput("Genre id must not be null");
        }
        if (!genreRepository.existsById(id)) {
            throw GenreExceptions.notFound(id);
        }
        genreRepository.deleteById(id);
    }
}