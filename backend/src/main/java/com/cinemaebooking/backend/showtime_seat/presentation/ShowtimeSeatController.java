package com.cinemaebooking.backend.showtime_seat.presentation;

import com.cinemaebooking.backend.showtime_seat.application.dto.ShowtimeSeatResponse;
import com.cinemaebooking.backend.showtime_seat.application.usecase.GetSeatMapByShowtimeUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Public API: Seat map theo showtime
 */
@RestController
@RequestMapping("/api/v1/showtimes")
@RequiredArgsConstructor
public class ShowtimeSeatController {

    private final GetSeatMapByShowtimeUseCase getSeatMapByShowtimeUseCase;

    /**
     * GET /api/v1/showtimes/{showtimeId}/seats
     */
    @GetMapping("/{showtimeId}/seats")
    public List<ShowtimeSeatResponse> getSeats(@PathVariable Long showtimeId) {
        return getSeatMapByShowtimeUseCase.execute(showtimeId);
    }
}