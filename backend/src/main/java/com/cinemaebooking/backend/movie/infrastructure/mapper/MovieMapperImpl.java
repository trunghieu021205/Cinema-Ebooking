package com.cinemaebooking.backend.movie.infrastructure.mapper;

import com.cinemaebooking.backend.movie.domain.model.Movie;
import com.cinemaebooking.backend.movie.domain.valueobject.MovieId;
import com.cinemaebooking.backend.movie.infrastructure.persistence.entity.MovieJpaEntity;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MovieMapperImpl implements MovieMapper {

    private final GenreMapper genreMapper;

    @Override
    public MovieJpaEntity toEntity(Movie domain) {
        if (domain == null) return null;

        return MovieJpaEntity.builder()
                .id(domain.getId() != null ? domain.getId().getValue() : null)
                .title(domain.getTitle())
                .description(domain.getDescription())
                .duration(domain.getDuration())
                .ageRating(domain.getAgeRating())
                .releaseDate(domain.getReleaseDate())
                .status(domain.getStatus())
                .posterUrl(domain.getPosterUrl())
                .bannerUrl(domain.getBannerUrl())
                .director(domain.getDirector())
                .actors(domain.getActors())
                .rating(domain.getRating())
                .ratingCount(domain.getRatingCount())
                .genres(null)
                .build();
    }

    @Override
    public Movie toDomain(MovieJpaEntity entity) {
        if (entity == null) return null;

        return Movie.builder()
                .id(MovieId.ofNullable(entity.getId()))
                .title(entity.getTitle())
                .description(entity.getDescription())
                .duration(entity.getDuration())
                .ageRating(entity.getAgeRating())
                .releaseDate(entity.getReleaseDate())
                .status(entity.getStatus())
                .posterUrl(entity.getPosterUrl())
                .bannerUrl(entity.getBannerUrl())
                .director(entity.getDirector())
                .actors(entity.getActors())
                // Chỉ map genres nếu đã được khởi tạo (tránh LazyInitializationException)
                .genres(Hibernate.isInitialized(entity.getGenres()) && entity.getGenres() != null ?
                        entity.getGenres().stream()
                                .map(genreMapper::toDomain)
                                .collect(Collectors.toSet()) : null)
                .rating(entity.getRating())
                .ratingCount(entity.getRatingCount())
                .build();
    }

    @Override
    public void updateEntity(MovieJpaEntity entity, Movie domain) {
        if (entity == null || domain == null) return;

        entity.setTitle(domain.getTitle());
        entity.setDescription(domain.getDescription());
        entity.setDuration(domain.getDuration());
        entity.setAgeRating(domain.getAgeRating());
        entity.setReleaseDate(domain.getReleaseDate());
        entity.setStatus(domain.getStatus());
        entity.setPosterUrl(domain.getPosterUrl());
        entity.setBannerUrl(domain.getBannerUrl());
        entity.setDirector(domain.getDirector());
        entity.setActors(domain.getActors());
        entity.setRating(domain.getRating());
        entity.setRatingCount(domain.getRatingCount());
    }
}