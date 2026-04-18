package com.cinemaebooking.backend.movie.presentation;

import com.cinemaebooking.backend.movie.application.dto.CreateGenreRequest;
import com.cinemaebooking.backend.movie.application.dto.GenreResponse;
import com.cinemaebooking.backend.movie.application.dto.UpdateGenreRequest;
import com.cinemaebooking.backend.movie.application.usecase.*;
import com.cinemaebooking.backend.movie.domain.valueobject.GenreId;
import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/genres")
@RequiredArgsConstructor
public class GenreController {

    private final CreateGenreUseCase createGenreUseCase;
    private final UpdateGenreUseCase updateGenreUseCase;
    private final DeleteGenreUseCase deleteGenreUseCase;
    private final GetGenreListUseCase getGenreListUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GenreResponse createGenre(@RequestBody CreateGenreRequest request) {
        return createGenreUseCase.execute(request);
    }

    @PutMapping("/{id}")
    public GenreResponse updateGenre(@PathVariable Long id,
                                     @RequestBody UpdateGenreRequest request) {
        GenreId genreId = toGenreId(id);
        return updateGenreUseCase.execute(genreId, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGenre(@PathVariable Long id) {
        GenreId genreId = toGenreId(id);
        deleteGenreUseCase.execute(genreId);
    }

    @GetMapping
    public Page<GenreResponse> getGenreList(@PageableDefault(size = 20) Pageable pageable) {
        return getGenreListUseCase.execute(pageable);
    }

    private GenreId toGenreId(Long id) {
        if (id == null) {
            throw CommonExceptions.invalidInput("Genre id must not be null");
        }
        return GenreId.of(id);
    }
}