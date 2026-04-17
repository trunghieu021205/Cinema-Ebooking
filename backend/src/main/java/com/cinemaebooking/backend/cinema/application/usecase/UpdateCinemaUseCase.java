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
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

/**
 * UpdateCinemaUseCase - Handles updating an existing Cinema.
 * Flow:
 * 1. Validate input & business rules
 * 2. Load existing cinema
 * 3. Apply domain update
 * 4. Persist changes
 * 5. Map to response
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
        Cinema cinema = loadCinema(id);

        // ================== APPLY DOMAIN ==================
        applyUpdate(cinema, request);

        // ================== PERSIST ==================
        Cinema saved = persist(cinema);

        // ================== MAP RESPONSE ==================
        return mapper.toResponse(saved);
    }

    // ================== PRIVATE METHODS ==================

    private Cinema loadCinema(CinemaId id) {
        return cinemaRepository.findById(id)
                .orElseThrow(() ->
                        CinemaExceptions.notFound(id)
                );
    }

    private void applyUpdate(Cinema cinema, UpdateCinemaRequest request) {
        cinema.update(
                request.getName(),
                request.getAddress(),
                request.getCity(),
                request.getStatus()
        );
    }

    private Cinema persist(Cinema cinema) {
        try {
            return cinemaRepository.update(cinema);
        } catch (OptimisticLockException | ObjectOptimisticLockingFailureException ex) {
            throw CommonExceptions.concurrencyConflict();
        }
    }
}