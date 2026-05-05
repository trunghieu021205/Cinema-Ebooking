package com.cinemaebooking.backend.cinema.presentation;

import com.cinemaebooking.backend.cinema.application.dto.CinemaResponse;
import com.cinemaebooking.backend.cinema.application.dto.CreateCinemaRequest;
import com.cinemaebooking.backend.cinema.application.dto.UpdateCinemaRequest;
import com.cinemaebooking.backend.cinema.application.usecase.*;
import com.cinemaebooking.backend.cinema.domain.valueobject.CinemaId;
import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * CinemaController - REST API for Cinema resource.
 * Responsibility:
 * - Handle HTTP requests
 * - Delegate to use cases
 * - Return proper HTTP responses
 *
 * @author Hieu Nguyen
 * @since 2026
 */
@RestController
@RequestMapping("/api/v1/cinemas")
@RequiredArgsConstructor
public class CinemaController {

    private final CreateCinemaUseCase createCinemaUseCase;
    private final UpdateCinemaUseCase updateCinemaUseCase;
    private final DeleteCinemaUseCase deleteCinemaUseCase;
    private final GetCinemaDetailUseCase getCinemaDetailUseCase;
    private final GetCinemaListUseCase getCinemaListUseCase;

    // ================== CREATE ==================
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public CinemaResponse createCinema(@RequestBody CreateCinemaRequest request) {
        return createCinemaUseCase.execute(request);
    }

    // ================== UPDATE ==================

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public CinemaResponse updateCinema(
            @PathVariable Long id,
            @RequestBody UpdateCinemaRequest request) {

        CinemaId cinemaId = toCinemaId(id);
        return updateCinemaUseCase.execute(cinemaId, request);
    }

    // ================== DELETE ==================
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCinema(@PathVariable Long id) {

        CinemaId cinemaId = toCinemaId(id);
        deleteCinemaUseCase.execute(cinemaId);
    }

    // ================== DETAIL ==================
    @GetMapping("/{id}")
    @PermitAll
    public CinemaResponse getCinemaDetail(@PathVariable Long id) {

        CinemaId cinemaId = toCinemaId(id);
        return getCinemaDetailUseCase.execute(cinemaId);
    }

    // ================== LIST ==================
    @GetMapping
    @PermitAll
    public Page<CinemaResponse> getCinemaList(@PageableDefault(size = 8) Pageable pageable) {
        return getCinemaListUseCase.execute(pageable);
    }

    // ================== HELPER ==================
    private CinemaId toCinemaId(Long id) {
        if (id == null) {
            throw CommonExceptions.invalidInput("Cinema id must not be null");
        }
        return CinemaId.of(id);
    }
}