package com.cinemaebooking.backend.movie.application.usecase.movie;

import com.cinemaebooking.backend.movie.application.port.MovieRepository;
import com.cinemaebooking.backend.movie.domain.valueobject.MovieId;
import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.common.exception.domain.MovieExceptions;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteMovieUseCase {

    private final MovieRepository movieRepository;

    public void execute(MovieId id) {
        if (id == null) {
            throw CommonExceptions.invalidInput("Movie id must not be null");
        }
        if (!movieRepository.existsById(id)) {
            throw MovieExceptions.notFound(id);
        }
        movieRepository.deleteById(id);
    }
}