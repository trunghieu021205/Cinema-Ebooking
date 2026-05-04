package com.cinemaebooking.backend.showtime_seat.application.usecase;

import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.seat.application.port.seat.SeatRepository;
import com.cinemaebooking.backend.seat.domain.model.seat.Seat;
import com.cinemaebooking.backend.showtime_seat.application.dto.ShowtimeSeatResponse;
import com.cinemaebooking.backend.showtime_seat.application.port.ShowtimeSeatRepository;
import com.cinemaebooking.backend.showtime_seat.domain.model.ShowtimeSeat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetSeatMapByShowtimeUseCase {

    private final ShowtimeSeatRepository showtimeSeatRepository;
    private final SeatRepository seatRepository;

    public List<ShowtimeSeatResponse> execute(Long showtimeId) {

        if (showtimeId == null) {
            throw CommonExceptions.invalidInput("ShowtimeId must not be null");
        }

        var showtimeSeats = showtimeSeatRepository.findByShowtimeId(showtimeId);

        // lấy tất cả seatId
        List<Long> seatIds = showtimeSeats.stream()
                .map(ShowtimeSeat::getSeatId)
                .toList();

        // query seats
        List<Seat> seats = seatRepository.findAllByIds(seatIds);

        Map<Long, Seat> seatMap = seats.stream()
                .collect(Collectors.toMap(
                        s -> s.getId().getValue(),
                        s -> s
                ));

        return showtimeSeats.stream()
                .map(stSeat -> {
                    Seat seat = seatMap.get(stSeat.getSeatId());

                    if (seat == null) {
                        throw CommonExceptions.invalidInput("Seat not found: " + stSeat.getSeatId());
                    }

                    return ShowtimeSeatResponse.builder()
                            .seatId(seat.getId().getValue())
                            .rowLabel(seat.getRowLabel())
                            .columnNumber(seat.getColumnNumber())
                            .build();
                })
                .toList();
    }
}