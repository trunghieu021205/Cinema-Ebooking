package com.cinemaebooking.backend.showtime_seat.application.usecase;

import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.common.exception.domain.ShowtimeExceptions;
import com.cinemaebooking.backend.room_layout.application.port.roomLayoutSeat.RoomLayoutSeatRepository;
import com.cinemaebooking.backend.room_layout.domain.model.roomLayoutSeat.RoomLayoutSeat;
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
    private final RoomLayoutSeatRepository roomLayoutSeatRepository;
    private final ShowtimeSeatRepository showtimeSeatRepository;
    private final ShowtimeSeatLayoutMapper layoutMapper;

    public ShowtimeSeatLayoutResponse execute(ShowtimeId showtimeId) {
        if (showtimeId == null) throw CommonExceptions.invalidInput("ShowtimeId must not be null");

        Showtime showtime = showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> ShowtimeExceptions.notFound(showtimeId));

        Long roomLayoutId = showtime.getRoomLayoutId();
        if (roomLayoutId == null) {
            throw new IllegalStateException("Showtime has no associated room layout");
        }

        List<RoomLayoutSeat> allSeatsInLayout = roomLayoutSeatRepository.findByRoomLayoutId(roomLayoutId);

        List<ShowtimeSeat> showtimeSeats = showtimeSeatRepository.findByShowtimeId(showtimeId.getValue());
        Map<Long, ShowtimeSeatStatus> seatIdToStatus = showtimeSeats.stream()
                .collect(Collectors.toMap(
                        ShowtimeSeat::getRoomLayoutSeatId,
                        ShowtimeSeat::getStatus
                ));

        return layoutMapper.toLayoutResponse(allSeatsInLayout, seatIdToStatus);
    }
}