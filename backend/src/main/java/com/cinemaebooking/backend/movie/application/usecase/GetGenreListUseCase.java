package com.cinemaebooking.backend.movie.application.usecase;

import com.cinemaebooking.backend.movie.application.dto.GenreResponse;
import com.cinemaebooking.backend.movie.application.mapper.GenreResponseMapper;
import com.cinemaebooking.backend.movie.application.port.GenreRepository;
import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetGenreListUseCase {

    private final GenreRepository genreRepository;
    private final GenreResponseMapper responseMapper;

    public Page<GenreResponse> execute(Pageable pageable) {
        if (pageable == null) {
            throw CommonExceptions.invalidInput("Pageable must not be null");
        }
        return genreRepository.findAll(pageable)
                .map(responseMapper::toResponse);
    }
}