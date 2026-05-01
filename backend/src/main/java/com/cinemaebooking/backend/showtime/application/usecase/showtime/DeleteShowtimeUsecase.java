package com.cinemaebooking.backend.showtime.application.usecase.showtime;

import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.common.exception.domain.ShowtimeExceptions;
import com.cinemaebooking.backend.showtime.application.port.ShowtimeRepository;
import com.cinemaebooking.backend.showtime.domain.valueobject.ShowtimeId;
import com.cinemaebooking.backend.showtime_seat.application.port.ShowtimeSeatRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteShowtimeUsecase {

    private final ShowtimeRepository showtimeRepository;
    private final ShowtimeSeatRepository showtimeSeatRepository;

    @Transactional
    public void execute(ShowtimeId id) {

        // ================== VALIDATION ==================
        if (id == null) {
            throw CommonExceptions.invalidInput("Showtime id must not be null");
        }

        // ================== GET ENTITY ==================
        var showtime = showtimeRepository.findById(id)
                .orElseThrow(() -> ShowtimeExceptions.notFound(id));

        // ================== DELETE CHILD (HARD DELETE) ==================
        showtimeSeatRepository.deleteByShowtimeId(id.getValue());

        // ================== DELETE SHOWTIME (SOFT DELETE) ==================
        showtimeRepository.deleteById(id);

        showtimeRepository.update(showtime);
    }
}
