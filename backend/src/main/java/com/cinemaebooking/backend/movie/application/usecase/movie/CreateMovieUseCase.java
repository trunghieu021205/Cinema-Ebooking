
package com.cinemaebooking.backend.movie.application.usecase.movie;

import com.cinemaebooking.backend.movie.application.dto.movie.CreateMovieRequest;
import com.cinemaebooking.backend.movie.application.dto.movie.MovieResponse;
import com.cinemaebooking.backend.movie.application.mapper.MovieResponseMapper;
import com.cinemaebooking.backend.movie.application.port.GenreRepository;
import com.cinemaebooking.backend.movie.application.port.MovieRepository;
import com.cinemaebooking.backend.movie.application.validator.MovieCommandValidator;
import com.cinemaebooking.backend.movie.domain.enums.MovieStatus;
import com.cinemaebooking.backend.movie.domain.model.Genre;
import com.cinemaebooking.backend.movie.domain.model.Movie;
import com.cinemaebooking.backend.movie.domain.valueobject.GenreId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CreateMovieUseCase {

    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;
    private final MovieResponseMapper mapper;
    private final MovieCommandValidator validator;

    public MovieResponse execute(CreateMovieRequest request) {
        validator.validateCreateRequest(request);

        Movie movie = buildMovie(request);
        Movie saved = movieRepository.create(movie);

        return mapper.toResponse(saved);
    }

    private Movie buildMovie(CreateMovieRequest request) {
        Set<Genre> genres = resolveGenres(request.getGenreIds());

        return Movie.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .duration(request.getDuration())
                .ageRating(request.getAgeRating())
                .releaseDate(request.getReleaseDate())
                .status(MovieStatus.COMING_SOON)
                .posterUrl(request.getPosterUrl())
                .bannerUrl(request.getBannerUrl())
                .director(request.getDirector())
                .actors(request.getActors())
                .genres(genres)
                .build();
    }

    private Set<Genre> resolveGenres(Set<Long> genreIds) {
        if (genreIds == null || genreIds.isEmpty()) return Set.of();
        Set<GenreId> ids = genreIds.stream()
                .map(GenreId::of)
                .collect(Collectors.toSet());
        return genreRepository.findAllByIds(ids);
    }
}
