package com.cinemaebooking.backend.showtime.presentation;

import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.showtime.application.dto.showtime.ShowtimeResponse;
import com.cinemaebooking.backend.showtime.application.usecase.showtime.GetShowtimeDetailUsecase;
import com.cinemaebooking.backend.showtime.application.usecase.showtime.GetShowtimeUseCase;
import com.cinemaebooking.backend.showtime.domain.valueobject.ShowtimeId;
import com.cinemaebooking.backend.showtime_seat.application.dto.ShowtimeSeatLayoutResponse;
import com.cinemaebooking.backend.showtime_seat.application.usecase.GetSeatMapByShowtimeUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/showtimes")
@RequiredArgsConstructor
public class ShowtimePublicController {

    private final GetShowtimeUseCase getShowtimesUseCase;
    private final GetShowtimeDetailUsecase getShowtimeDetailUseCase;
    private final GetSeatMapByShowtimeUseCase getSeatMapByShowtimeUseCase;

    // ================== LIST (PUBLIC) ==================
    @GetMapping
    public Page<ShowtimeResponse> searchShowtimes(
            @RequestParam(required = false) Long cinemaId,
            @RequestParam(required = false) Long movieId,
            @RequestParam(required = false) LocalDate date,
            @PageableDefault(size = 8) Pageable pageable
    ) {
        return getShowtimesUseCase.execute(cinemaId, movieId,null, null, date, pageable);
    }

    // ================== DETAIL (PUBLIC) ==================
    @GetMapping("/{id}")
    public ShowtimeResponse getShowtimeById(@PathVariable Long id) {
        return getShowtimeDetailUseCase.execute(toShowtimeId(id));
    }


    @GetMapping("/{id}/seats")
    public ShowtimeSeatLayoutResponse getSeatMap(@PathVariable Long id) {
        return getSeatMapByShowtimeUseCase.execute(toShowtimeId(id));
    }

    // ================== HELPER ==================
    private ShowtimeId toShowtimeId(Long id) {
        if (id == null) {
            throw CommonExceptions.invalidInput("Showtime id must not be null");
        }
        return new ShowtimeId(id);
    }
}