package com.cinemaebooking.backend.movie.application.usecase;

import com.cinemaebooking.backend.movie.application.dto.CreateGenreRequest;
import com.cinemaebooking.backend.movie.application.dto.GenreResponse;
import com.cinemaebooking.backend.movie.application.mapper.GenreResponseMapper;
import com.cinemaebooking.backend.movie.application.port.GenreRepository;
import com.cinemaebooking.backend.movie.application.validator.GenreCommandValidator;
import com.cinemaebooking.backend.movie.domain.model.Genre;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateGenreUseCase {

    private final GenreRepository genreRepository;
    private final GenreResponseMapper responseMapper;
    private final GenreCommandValidator validator;

    public GenreResponse execute(CreateGenreRequest request) {
        validator.validateCreateRequest(request);

        Genre genre = Genre.builder()
                .name(request.getName())
                .build();

        Genre saved = genreRepository.create(genre);
        return responseMapper.toResponse(saved);
    }
}