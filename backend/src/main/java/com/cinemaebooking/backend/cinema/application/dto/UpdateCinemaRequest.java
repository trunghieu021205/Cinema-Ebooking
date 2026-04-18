package com.cinemaebooking.backend.cinema.application.dto;

import com.cinemaebooking.backend.cinema.domain.enums.CinemaStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * UpdateCinemaRequest represents the payload used to update an existing Cinema.
 *
 * @author Hieu Nguyen
 * @since 2026
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCinemaRequest {

    private String name;

    private String address;

    private String city;

    private CinemaStatus status;
}