package com.cinemaebooking.backend.bootstrap.seed;

import com.cinemaebooking.backend.cinema.application.port.CinemaRepository;
import com.cinemaebooking.backend.cinema.domain.enums.CinemaStatus;
import com.cinemaebooking.backend.cinema.domain.model.Cinema;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class CinemaSeed {

    private final CinemaRepository cinemaRepository;

    private static final String NAME = "CGV Vincom";
    private static final String CITY = "Da Nang";

    public Cinema seed() {

        if (cinemaRepository.existsByName(NAME)) {
            log.info("Cinema already exists: {}", NAME);
            return cinemaRepository.findByName(NAME); // 👈 FIX
        }

        Cinema cinema = Cinema.builder()
                .name(NAME)
                .address("123 Le Loi")
                .city(CITY)
                .status(CinemaStatus.ACTIVE)
                .build();

        Cinema savedCinema = cinemaRepository.create(cinema);

        log.info("Cinema seeded: {}", NAME);
        return savedCinema;
    }
}
