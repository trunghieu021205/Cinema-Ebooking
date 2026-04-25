package com.cinemaebooking.backend.movie.application.usecase.movie;

import com.cinemaebooking.backend.movie.application.dto.movie.MovieResponse;
import com.cinemaebooking.backend.movie.application.mapper.MovieResponseMapper;
import com.cinemaebooking.backend.movie.application.port.MovieRepository;
import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetMovieListUseCase {

    private final MovieRepository movieRepository;
    private final MovieResponseMapper mapper;

    public Page<MovieResponse> execute(Pageable pageable) {
        if (pageable == null) {
            throw CommonExceptions.invalidInput("Pageable must not be null");
        }
        return movieRepository.findAll(pageable).map(mapper::toResponse);
    }
}