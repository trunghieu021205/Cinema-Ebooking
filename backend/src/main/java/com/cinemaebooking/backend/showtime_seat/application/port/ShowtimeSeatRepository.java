package com.cinemaebooking.backend.showtime_seat.application.port;

import com.cinemaebooking.backend.showtime_seat.domain.enums.ShowtimeSeatStatus;
import com.cinemaebooking.backend.showtime_seat.domain.model.ShowtimeSeat;

import java.util.List;
import java.util.Optional;

public interface ShowtimeSeatRepository {
    void save(ShowtimeSeat showtimeSeat);

    /**
     * Insert hàng loạt seat mapping cho 1 showtime.
     * (được gọi trong CreateShowtimeUseCase)
     */
    void saveAll(List<ShowtimeSeat> seats);

    /**
     * Trả về danh sách seat mapping để dựng seat map UI.
     */
    List<ShowtimeSeat> findByShowtimeId(Long showtimeId);

    /**
     * Hard delete toàn bộ mapping của suất chiếu.
     * (gọi trong DeleteShowtimeUseCase)
     */
    void deleteByShowtimeId(Long showtimeId);

    void updateStatusToAvailable(Long showtimeId, List<Long> showtimeSeatIds);
    List<ShowtimeSeat> findAllByIds(List<Long> seatIds);
}