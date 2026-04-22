package com.cinemaebooking.backend.movie.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GenreResponse {
    private Long id;
    private String name;
}