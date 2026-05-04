package com.cinemaebooking.backend.showtime_seat.application.usecase;

import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.common.exception.domain.ShowtimeExceptions;
import com.cinemaebooking.backend.seat.application.port.seat.SeatRepository;
import com.cinemaebooking.backend.seat.domain.model.seat.Seat;
import com.cinemaebooking.backend.showtime.application.port.ShowtimeRepository;
import com.cinemaebooking.backend.showtime.domain.model.Showtime;
import com.cinemaebooking.backend.showtime.domain.valueobject.ShowtimeId;
import com.cinemaebooking.backend.showtime_seat.application.dto.ShowtimeSeatLayoutResponse;
import com.cinemaebooking.backend.showtime_seat.application.mapper.ShowtimeSeatLayoutMapper;
import com.cinemaebooking.backend.showtime_seat.application.port.ShowtimeSeatRepository;
import com.cinemaebooking.backend.showtime_seat.domain.enums.ShowtimeSeatStatus;
import com.cinemaebooking.backend.showtime_seat.domain.model.ShowtimeSeat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetSeatMapByShowtimeUseCase {

    private final ShowtimeRepository showtimeRepository;
    private final SeatRepository seatRepository;
    private final ShowtimeSeatRepository showtimeSeatRepository;
    private final ShowtimeSeatLayoutMapper layoutMapper;  // inject mapper

    public ShowtimeSeatLayoutResponse execute(ShowtimeId showtimeId) {
        if (showtimeId == null) {
            throw CommonExceptions.invalidInput("ShowtimeId must not be null");
        }

        Showtime showtime = showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> ShowtimeExceptions.notFound(showtimeId));
        Long roomId = showtime.getRoomId();

        // Lấy tất cả ghế trong phòng
        List<Seat> allSeatsInRoom = seatRepository.findByRoomId(roomId);

        // Lấy trạng thái từ showtime_seat
        List<ShowtimeSeat> showtimeSeats = showtimeSeatRepository.findByShowtimeId(showtimeId.getValue());
        Map<Long, ShowtimeSeatStatus> seatIdToStatus = showtimeSeats.stream()
                .collect(Collectors.toMap(
                        ShowtimeSeat::getSeatId,
                        ShowtimeSeat::getStatus
                ));

        // Dùng mapper để tạo response
        return layoutMapper.toLayoutResponse(allSeatsInRoom, seatIdToStatus);
    }
}