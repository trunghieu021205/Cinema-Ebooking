package com.cinemaebooking.backend.movie.presentation;

import com.cinemaebooking.backend.movie.application.dto.genre.CreateGenreRequest;
import com.cinemaebooking.backend.movie.application.dto.genre.GenreResponse;
import com.cinemaebooking.backend.movie.application.dto.genre.UpdateGenreRequest;
import com.cinemaebooking.backend.movie.application.usecase.genre.*;
import com.cinemaebooking.backend.movie.domain.valueobject.GenreId;
import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/genres")
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
    public GenreResponse updateGenre(@PathVariable Long id, @RequestBody UpdateGenreRequest request) {
        return updateGenreUseCase.execute(toGenreId(id), request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGenre(@PathVariable Long id) {
        deleteGenreUseCase.execute(toGenreId(id));
    }

    @GetMapping
    public Page<GenreResponse> getGenreList(@PageableDefault(size = 20) Pageable pageable) {
        return getGenreListUseCase.execute(pageable);
    }

    private GenreId toGenreId(Long id) {
        if (id == null) throw CommonExceptions.invalidInput("Genre id must not be null");
        return GenreId.of(id);
    }
}