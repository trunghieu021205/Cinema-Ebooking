package com.cinemaebooking.backend.movie.application.dto.genre;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GenreResponse {
    private Long id;
    private String name;
}