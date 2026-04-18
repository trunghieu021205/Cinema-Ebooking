package com.cinemaebooking.backend.cinema.application.usecase;

import com.cinemaebooking.backend.cinema.application.dto.CinemaResponse;
import com.cinemaebooking.backend.cinema.application.dto.CreateCinemaRequest;
import com.cinemaebooking.backend.cinema.application.mapper.CinemaResponseMapper;
import com.cinemaebooking.backend.cinema.application.port.CinemaRepository;
import com.cinemaebooking.backend.cinema.application.validator.CinemaCommandValidator;
import com.cinemaebooking.backend.cinema.domain.enums.CinemaStatus;
import com.cinemaebooking.backend.cinema.domain.model.Cinema;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * CreateCinemaUseCase - Handles creation of a new Cinema.
 * Flow:
 * 1. Validate request
 * 2. Build domain object
 * 3. Persist entity
 * 4. Map response
 * @author Hieu Nguyen
 * @since 2026
 */
@Service
@RequiredArgsConstructor
public class CreateCinemaUseCase {

    private final CinemaRepository cinemaRepository;
    private final CinemaResponseMapper mapper;
    private final CinemaCommandValidator validator;

    public CinemaResponse execute(CreateCinemaRequest request) {

        // ================== VALIDATION ==================
        validator.validateCreateRequest(request);

        // ================== BUILD DOMAIN ==================
        Cinema cinema = buildCinema(request);

        // ================== PERSIST ==================
        Cinema saved = cinemaRepository.create(cinema);

        // ================== RESPONSE ==================
        return mapper.toResponse(saved);
    }

    // ================== PRIVATE METHODS ==================

    private Cinema buildCinema(CreateCinemaRequest request) {
        return Cinema.builder()
                .name(request.getName())
                .address(request.getAddress())
                .city(request.getCity())
                .status(CinemaStatus.ACTIVE) // default business rule
                .build();
    }
}