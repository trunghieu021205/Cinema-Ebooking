package com.cinemaebooking.backend.showtime.presentation;

import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.showtime.application.dto.showtime.CreateShowtimeRequest;
import com.cinemaebooking.backend.showtime.application.dto.showtime.ShowtimeResponse;
import com.cinemaebooking.backend.showtime.application.dto.showtime.UpdateShowtimeRequest;
import com.cinemaebooking.backend.showtime.application.usecase.showtime.*;
import com.cinemaebooking.backend.showtime.domain.valueobject.ShowtimeId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/admin/showtimes")
@RequiredArgsConstructor
public class ShowtimeAdminController {

    private final CreateShowtimeUseCase createShowtimeUseCase;
    private final UpdateShowtimeUsecase updateShowtimeUseCase;
    private final DeleteShowtimeUsecase deleteShowtimeUseCase;
    private final GetShowtimeUseCase getShowtimeUsecase;
    private final GetShowtimeDetailUsecase getShowtimeDetailUseCase;
    private final CancelShowtimeUseCase cancelShowtimeUseCase;

    // ================== CREATE ==================
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ShowtimeResponse createShowtime(@Valid @RequestBody CreateShowtimeRequest request) {
        return createShowtimeUseCase.execute(request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public Page<ShowtimeResponse> getShowtimes(
            @RequestParam(required = false) Long cinemaId,
            @RequestParam(required = false) Long movieId,
            @RequestParam(required = false) Long roomId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Pageable pageable
    ) {
        return getShowtimeUsecase.execute(cinemaId, movieId, roomId, status, date, pageable);
    }

    // ================== DETAIL ==================
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ShowtimeResponse getShowtimeById(@PathVariable Long id) {
        return getShowtimeDetailUseCase.execute(toShowtimeId(id));
    }

    // ================== UPDATE ==================
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ShowtimeResponse updateShowtime(
            @PathVariable Long id,
            @Valid @RequestBody UpdateShowtimeRequest request
    ) {
        return updateShowtimeUseCase.execute(toShowtimeId(id), request);
    }

    // ================== CANCEL ==================
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/cancel")
    public ShowtimeResponse cancel(@PathVariable Long id) {
        return cancelShowtimeUseCase.execute(toShowtimeId(id));
    }

    // ================== DELETE ==================
    /*@PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteShowtime(@PathVariable Long id) {
        deleteShowtimeUseCase.execute(toShowtimeId(id));
    }*/

    // ================== HELPER ==================
    private ShowtimeId toShowtimeId(Long id) {
        if (id == null) {
            throw CommonExceptions.invalidInput("Showtime id must not be null");
        }
        return new ShowtimeId(id);
    }
}