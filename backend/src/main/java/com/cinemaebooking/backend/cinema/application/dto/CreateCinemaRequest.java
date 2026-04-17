package com.cinemaebooking.backend.cinema.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * CreateCinemaRequest represents the payload used to create a new Cinema.
 *
 * @author Hieu Nguyen
 * @since 2026
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCinemaRequest {

    private String name;

    private String address;

    private String city;
}