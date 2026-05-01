package com.cinemaebooking.backend.showtime.presentation;

import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.showtime.application.dto.showtime.CreateShowtimeRequest;
import com.cinemaebooking.backend.showtime.application.dto.showtime.ShowtimeResponse;
import com.cinemaebooking.backend.showtime.application.dto.showtime.UpdateShowtimeRequest;
import com.cinemaebooking.backend.showtime.application.usecase.showtime.*;
import com.cinemaebooking.backend.showtime.domain.valueobject.ShowtimeId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/showtimes")
@RequiredArgsConstructor
public class ShowtimeAdminController {

    private final CreateShowtimeUsecase createShowtimeUseCase;
    private final UpdateShowtimeUsecase updateShowtimeUseCase;
    private final DeleteShowtimeUsecase deleteShowtimeUseCase;
    private final GetShowtimeDetailUsecase getShowtimeDetailUseCase;

    // ================== CREATE ==================
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ShowtimeResponse createShowtime(@Valid @RequestBody CreateShowtimeRequest request) {
        return createShowtimeUseCase.execute(request);
    }

    // ================== DETAIL ==================
    @GetMapping("/{id}")
    public ShowtimeResponse getShowtimeById(@PathVariable Long id) {
        return getShowtimeDetailUseCase.execute(toShowtimeId(id));
    }

    // ================== UPDATE ==================
    @PutMapping("/{id}")
    public ShowtimeResponse updateShowtime(
            @PathVariable Long id,
            @Valid @RequestBody UpdateShowtimeRequest request
    ) {
        return updateShowtimeUseCase.execute(toShowtimeId(id), request);
    }

    // ================== DELETE ==================
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteShowtime(@PathVariable Long id) {
        deleteShowtimeUseCase.execute(toShowtimeId(id));
    }

    // ================== HELPER ==================
    private ShowtimeId toShowtimeId(Long id) {
        if (id == null) {
            throw CommonExceptions.invalidInput("Showtime id must not be null");
        }
        return new ShowtimeId(id);
    }
}