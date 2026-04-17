package com.cinemaebooking.backend.cinema.application.usecase;

import com.cinemaebooking.backend.cinema.application.dto.CinemaResponse;
import com.cinemaebooking.backend.cinema.application.mapper.CinemaResponseMapper;
import com.cinemaebooking.backend.cinema.domain.valueobject.CinemaId;
import com.cinemaebooking.backend.cinema.application.port.CinemaRepository;
import com.cinemaebooking.backend.common.exception.domain.CinemaExceptions;
import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * GetCinemaDetailUseCase - Handles retrieving cinema detail by ID.
 * Responsibility:
 * - Validate input
 * - Fetch cinema from repository
 * - Map domain → response DTO
 *
 * @author Hieu Nguyen
 * @since 2026
 */
@Service
@RequiredArgsConstructor
public class GetCinemaDetailUseCase {

    private final CinemaRepository cinemaRepository;
    private final CinemaResponseMapper mapper;

    /**
     * Get cinema detail by id.
     *
     * @param id cinema identifier
     * @return CinemaResponse
     */
    public CinemaResponse execute(CinemaId id) {

        // ================== INPUT VALIDATION ==================
        if (id == null) {
            throw CommonExceptions.invalidInput("Cinema id must not be null");
        }

        // ================== BUSINESS VALIDATION ==================
        return cinemaRepository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() ->
                        CinemaExceptions.notFound(id)
                );
    }
}