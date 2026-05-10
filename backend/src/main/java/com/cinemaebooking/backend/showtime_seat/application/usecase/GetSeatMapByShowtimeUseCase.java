package com.cinemaebooking.backend.showtime_seat.application.usecase;

import com.cinemaebooking.backend.common.exception.ErrorCategory;
import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.common.exception.domain.ShowtimeExceptions;

import com.cinemaebooking.backend.showtime.application.port.ShowtimeRepository;
import com.cinemaebooking.backend.showtime.domain.model.Showtime;
import com.cinemaebooking.backend.showtime.domain.valueobject.ShowtimeId;
import com.cinemaebooking.backend.showtime_seat.application.dto.ShowtimeSeatLayoutResponse;
import com.cinemaebooking.backend.showtime_seat.application.mapper.ShowtimeSeatLayoutMapper;
import com.cinemaebooking.backend.showtime_seat.application.port.ShowtimeSeatRepository;
import com.cinemaebooking.backend.showtime_seat.domain.model.ShowtimeSeat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class GetSeatMapByShowtimeUseCase {
    private final ShowtimeRepository showtimeRepository;
    private final ShowtimeSeatRepository showtimeSeatRepository;
    private final ShowtimeSeatLayoutMapper layoutMapper;

    public ShowtimeSeatLayoutResponse execute(ShowtimeId showtimeId) {
        if (showtimeId == null) throw CommonExceptions.invalidInput("showtimeId", ErrorCategory.REQUIRED,"ShowtimeId must not be null");

        Showtime showtime = showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> ShowtimeExceptions.notFound(showtimeId));

        // Lấy tất cả ShowtimeSeat của suất chiếu (đã snapshot đầy đủ thông tin)
        List<ShowtimeSeat> showtimeSeats = showtimeSeatRepository.findByShowtimeId(showtimeId.getValue());

        // Mapper sẽ dùng rowIndex, colIndex, seatNumber, seatTypeId, active, status có sẵn
        return layoutMapper.toLayoutResponse(showtimeSeats);
    }
}