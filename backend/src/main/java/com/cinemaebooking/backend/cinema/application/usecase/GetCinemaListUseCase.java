package com.cinemaebooking.backend.cinema.application.usecase;

import com.cinemaebooking.backend.cinema.application.dto.CinemaResponse;
import com.cinemaebooking.backend.cinema.application.mapper.CinemaResponseMapper;
import com.cinemaebooking.backend.cinema.application.port.CinemaRepository;
import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * GetCinemaListUseCase - Handles retrieving paginated cinema list.
 * Responsibility:
 * - Validate input (if needed)
 * - Fetch paginated data from repository
 * - Map domain → response DTO
 *
 * @author Hieu Nguyen
 * @since 2026
 */
@Service
@RequiredArgsConstructor
public class GetCinemaListUseCase {

    private final CinemaRepository cinemaRepository;
    private final CinemaResponseMapper mapper;

    /**
     * Get paginated list of cinemas.
     *
     * @param pageable pagination info
     * @return page of CinemaResponse
     */
    public Page<CinemaResponse> execute(Pageable pageable) {

        // ================== INPUT VALIDATION ==================
        if (pageable == null) {
            throw CommonExceptions.invalidInput("Pageable must not be null");
        }

        // ================== BUSINESS ==================
        return cinemaRepository.findAll(pageable)
                .map(mapper::toResponse);
    }
}