package com.cinemaebooking.backend.showtime_seat.presentation;

import com.cinemaebooking.backend.showtime.domain.valueobject.ShowtimeId;
import com.cinemaebooking.backend.showtime_seat.application.dto.ShowtimeSeatLayoutResponse;
import com.cinemaebooking.backend.showtime_seat.application.dto.ShowtimeSeatResponse;
import com.cinemaebooking.backend.showtime_seat.application.usecase.GetSeatMapByShowtimeUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Public API: Seat map theo showtime
 */
@RestController
@RequestMapping("/api/v1/showtime_seats")
@RequiredArgsConstructor
public class ShowtimeSeatController {

    private final GetSeatMapByShowtimeUseCase getSeatMapByShowtimeUseCase;

    /**
     * GET /api/v1/showtimes/{showtimeId}/seats
     */
    @GetMapping("/{showtimeId}/seat_layout")
    public ShowtimeSeatLayoutResponse getSeats(@PathVariable Long showtimeId) {
        return getSeatMapByShowtimeUseCase.execute(toShowtimeId(showtimeId));
    }

    public ShowtimeId toShowtimeId(Long id) {
        return ShowtimeId.of(id);
    }

}