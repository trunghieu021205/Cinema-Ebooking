package com.cinemaebooking.backend.showtime.presentation;

import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.showtime.application.dto.showtime.ShowtimeResponse;
import com.cinemaebooking.backend.showtime.application.usecase.showtime.GetShowtimeDetailUsecase;
import com.cinemaebooking.backend.showtime.application.usecase.showtime.GetShowtimeUsecase;
import com.cinemaebooking.backend.showtime.domain.valueobject.ShowtimeId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/showtimes_admin")
@RequiredArgsConstructor
public class ShowtimePublicController {

    private final GetShowtimeUsecase getShowtimesUseCase;
    private final GetShowtimeDetailUsecase getShowtimeDetailUseCase;

    // ================== LIST (PUBLIC) ==================
    @GetMapping
    public Page<ShowtimeResponse> searchShowtimes(
            @RequestParam(required = false) Long cinemaId,
            @RequestParam(required = false) Long movieId,
            @RequestParam(required = false) LocalDate date,
            @PageableDefault(size = 8) Pageable pageable
    ) {
        return getShowtimesUseCase.execute(cinemaId, movieId, date, pageable);
    }

    // ================== DETAIL (PUBLIC) ==================
    @GetMapping("/{id}")
    public ShowtimeResponse getShowtimeById(@PathVariable Long id) {
        return getShowtimeDetailUseCase.execute(toShowtimeId(id));
    }

    // ================== HELPER ==================
    private ShowtimeId toShowtimeId(Long id) {
        if (id == null) {
            throw CommonExceptions.invalidInput("Showtime id must not be null");
        }
        return new ShowtimeId(id);
    }
}