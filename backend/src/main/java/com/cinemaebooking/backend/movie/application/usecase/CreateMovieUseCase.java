package com.cinemaebooking.backend.movie.application.usecase;

import com.cinemaebooking.backend.movie.application.dto.CreateMovieRequest;
import com.cinemaebooking.backend.movie.application.dto.MovieResponse;
import com.cinemaebooking.backend.movie.application.mapper.MovieResponseMapper;
import com.cinemaebooking.backend.movie.application.port.MovieRepository;
import com.cinemaebooking.backend.movie.application.validator.MovieCommandValidator;
import com.cinemaebooking.backend.movie.domain.enums.MovieStatus;
import com.cinemaebooking.backend.movie.domain.model.Movie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * CreateMovieUseCase - Handles creation of a new Movie.
 *
 * @author Hieu Nguyen
 * @since 2026
 */
@Service
@RequiredArgsConstructor
public class CreateMovieUseCase {

    private final MovieRepository movieRepository;
    private final MovieResponseMapper responseMapper;
    private final MovieCommandValidator validator;

    public MovieResponse execute(CreateMovieRequest request) {
        validator.validateCreateRequest(request);

        Movie movie = buildMovie(request);
        Movie saved = movieRepository.create(movie, request.getGenreIds());
        return responseMapper.toResponse(saved);
    }

    private Movie buildMovie(CreateMovieRequest request) {
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
                .rating(0.0)
                .ratingCount(0)
                .build();
    }
}