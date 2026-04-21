package com.cinemaebooking.backend.movie.domain.model;

import com.cinemaebooking.backend.common.domain.BaseEntity;
import com.cinemaebooking.backend.movie.domain.enums.AgeRating;
import com.cinemaebooking.backend.movie.domain.enums.MovieStatus;
import com.cinemaebooking.backend.movie.domain.valueobject.MovieId;
import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@SuperBuilder
public class Movie extends BaseEntity<MovieId> {

    private String title;
    private String description;
    private Integer duration;
    private AgeRating ageRating;
    private LocalDate releaseDate;
    private MovieStatus status;
    private String posterUrl;
    private String bannerUrl;
    private String director;
    private String actors;
    private Set<Genre> genres;
    private Double rating;
    private Integer ratingCount;

    public void updateInfo(String title, String description, Integer duration,
                           AgeRating ageRating, LocalDate releaseDate,
                           String posterUrl, String bannerUrl,
                           String director, String actors) {
        validateBasicInfo(title, duration, ageRating, releaseDate);
        this.title = title;
        this.description = description;
        this.duration = duration;
        this.ageRating = ageRating;
        this.releaseDate = releaseDate;
        this.posterUrl = posterUrl;
        this.bannerUrl = bannerUrl;
        this.director = director;
        this.actors = actors;
    }

    public void changeStatus(MovieStatus status) {
        if (status == null) {
            throw CommonExceptions.invalidInput("Movie status must not be null");
        }
        this.status = status;
    }

    public void addGenre(Genre genre) {
        if (genre == null) {
            throw CommonExceptions.invalidInput("Genre must not be null");
        }
        if (this.genres == null) {
            this.genres = new HashSet<>();
        }
        this.genres.add(genre);
    }

    public void removeGenre(Genre genre) {
        if (genre == null) return;
        if (this.genres != null) {
            this.genres.remove(genre);
        }
    }

    public void clearGenres() {
        if (this.genres != null) {
            this.genres.clear();
        }
    }

    private void validateBasicInfo(String title, Integer duration,
                                   AgeRating ageRating, LocalDate releaseDate) {
        if (title == null || title.trim().isEmpty()) {
            throw CommonExceptions.invalidInput("Movie title must not be empty");
        }
        if (duration == null || duration <= 0) {
            throw CommonExceptions.invalidInput("Duration must be positive");
        }
        if (ageRating == null) {
            throw CommonExceptions.invalidInput("Age rating must not be null");
        }
        if (releaseDate == null) {
            throw CommonExceptions.invalidInput("Release date must not be null");
        }
    }
}