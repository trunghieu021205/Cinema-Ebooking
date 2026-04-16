package com.cinemaebooking.backend.cinema.application.usecase;

import com.cinemaebooking.backend.cinema.application.dto.CinemaResponse;
import com.cinemaebooking.backend.cinema.application.dto.UpdateCinemaRequest;
import com.cinemaebooking.backend.cinema.application.mapper.CinemaResponseMapper;
import com.cinemaebooking.backend.cinema.application.port.CinemaRepository;
import com.cinemaebooking.backend.cinema.application.validator.CinemaCommandValidator;
import com.cinemaebooking.backend.cinema.domain.model.Cinema;
import com.cinemaebooking.backend.cinema.domain.valueobject.CinemaId;
import com.cinemaebooking.backend.common.exception.domain.CinemaExceptions;
import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import jakarta.persistence.OptimisticLockException;

/**
 * UpdateCinemaUseCase - Handles updating an existing Cinema.
 * Flow:
 * 1. Validate input & business rules
 * 2. Load entity
 * 3. Apply domain changes
 * 4. Persist
 *
 * @author Hieu Nguyen
 * @since 2026
 */
@Service
@RequiredArgsConstructor
public class UpdateCinemaUseCase {

    private final CinemaRepository cinemaRepository;
    private final CinemaResponseMapper mapper;
    private final CinemaCommandValidator validator;

    public CinemaResponse execute(CinemaId id, UpdateCinemaRequest request) {

        // ================== VALIDATION ==================
        validator.validateUpdateRequest(id, request);

        // ================== LOAD ==================
        Cinema cinema = cinemaRepository.findById(id)
                .orElseThrow(() ->
                        CinemaExceptions.notFound("Cinema not found with id: " + id)
                );

        // ================== APPLY DOMAIN ==================
        Cinema updatedCinema = cinema.toBuilder()
                .name(request.getName())
                .address(request.getAddress())
                .city(request.getCity())
                .status(request.getStatus())
                .build();

        // ================== PERSIST ==================
        try {
            Cinema saved = cinemaRepository.update(updatedCinema);
            return mapper.toResponse(saved);

        } catch (OptimisticLockException | ObjectOptimisticLockingFailureException ex) {
            throw CommonExceptions.concurrencyConflict();
        }
    }
}