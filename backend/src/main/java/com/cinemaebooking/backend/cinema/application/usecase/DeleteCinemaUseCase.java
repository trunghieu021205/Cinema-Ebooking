package com.cinemaebooking.backend.cinema.application.usecase;

import com.cinemaebooking.backend.cinema.application.port.CinemaRepository;
import com.cinemaebooking.backend.cinema.domain.valueobject.CinemaId;
import com.cinemaebooking.backend.common.exception.domain.CinemaExceptions;
import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * DeleteCinemaUseCase - Handles soft delete of a Cinema.
 *
 * Responsibility:
 * - Validate existence of Cinema
 * - Perform soft delete via repository
 * - Ensure consistent domain exception handling
 *
 * @author Hieu Nguyen
 * @since 2026
 */
@Service
@RequiredArgsConstructor
public class DeleteCinemaUseCase {

    private final CinemaRepository cinemaRepository;

    /**
     * Soft delete Cinema by id.
     *
     * @param id cinema identifier
     */
    public void execute(CinemaId id) {

        // ================== INPUT VALIDATION ==================
        if (id == null) {
            throw CommonExceptions.invalidInput("Cinema id must not be null");
        }

        // ================== BUSINESS VALIDATION ==================
        if (!cinemaRepository.existsById(id)) {
            throw CinemaExceptions.notFound(id);
        }

        // ================== DELETE ==================
        cinemaRepository.deleteById(id);
    }
}