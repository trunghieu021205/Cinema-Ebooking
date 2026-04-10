package com.cinemaebooking.backend.cinema.application.mapper;

import com.cinemaebooking.backend.cinema.application.dto.CinemaResponse;
import com.cinemaebooking.backend.cinema.domain.model.Cinema;
import org.springframework.stereotype.Component;

@Component
public class CinemaResponseMapper {

    public CinemaResponse toResponse(Cinema cinema) {
        if (cinema == null) return null;

        return new CinemaResponse(
                cinema.getId() != null ? cinema.getId().getValue() : null,
                cinema.getName(),
                cinema.getAddress(),
                cinema.getCity(),
                cinema.getStatus()
        );
    }
}