package com.cinemaebooking.backend.cinema.application.dto;

import com.cinemaebooking.backend.cinema.domain.enums.CinemaStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * CinemaResponse represents the API response model for Cinema data.
 *
 * @author Hieu Nguyen
 * @since 2026
 */
@Getter
@AllArgsConstructor
public class CinemaResponse {

    private Long id;

    private String name;

    private String address;

    private String city;

    private CinemaStatus status;
}