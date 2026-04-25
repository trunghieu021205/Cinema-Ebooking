package com.cinemaebooking.backend.movie.application.usecase.movie;

import com.cinemaebooking.backend.movie.application.dto.movie.MovieResponse;
import com.cinemaebooking.backend.movie.application.mapper.MovieResponseMapper;
import com.cinemaebooking.backend.movie.application.port.MovieRepository;
import com.cinemaebooking.backend.movie.domain.valueobject.MovieId;
import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.common.exception.domain.MovieExceptions;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetMovieDetailUseCase {

    private final MovieRepository movieRepository;
    private final MovieResponseMapper mapper;

    public MovieResponse execute(MovieId id) {
        if (id == null) {
            throw CommonExceptions.invalidInput("Movie id must not be null");
        }
        return movieRepository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> MovieExceptions.notFound(id));
    }
}