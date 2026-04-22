package com.cinemaebooking.backend.movie.application.dto.movie;

import com.cinemaebooking.backend.movie.domain.enums.AgeRating;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateMovieRequest {
    private String title;
    private String description;
    private Integer duration;
    private AgeRating ageRating;
    private LocalDate releaseDate;
    private String posterUrl;
    private String bannerUrl;
    private String director;
    private String actors;
    private Set<Long> genreIds;
}