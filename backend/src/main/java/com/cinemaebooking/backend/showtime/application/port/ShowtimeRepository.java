package com.cinemaebooking.backend.showtime.application.port;

import com.cinemaebooking.backend.showtime.domain.model.Showtime;
import com.cinemaebooking.backend.showtime.domain.valueobject.ShowtimeId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public interface ShowtimeRepository {

    Showtime create(Showtime showtime);

    Showtime update(Showtime showtime);

    Optional<Showtime> findById(ShowtimeId id);

    void deleteById(ShowtimeId id);

    boolean existsById(ShowtimeId id);

    Page<Showtime> search(
            Long cinemaId,
            Long movieId,
            LocalDate date,
            Pageable pageable
    );

    boolean existsRoomConflict(
            Long roomId,
            LocalDateTime startTime,
            LocalDateTime endTime,
            ShowtimeId excludeId
    );
}