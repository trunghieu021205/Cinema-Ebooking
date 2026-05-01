package com.cinemaebooking.backend.seat.application.usecase.seat;


import com.cinemaebooking.backend.common.exception.domain.CommonExceptions;
import com.cinemaebooking.backend.seat.application.dto.seat.SeatResponse;
import com.cinemaebooking.backend.seat.application.mapper.seat.SeatResponseMapper;
import com.cinemaebooking.backend.seat.application.port.seat.SeatRepository;
import com.cinemaebooking.backend.seat.domain.model.seat.Seat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class FindSeatsByRoomIdUsecase {

    private final SeatRepository seatRepository;
    private final SeatResponseMapper mapper;

    public List<SeatResponse> execute(Long roomId) {

        // Validate input
        if (roomId == null) {
            throw CommonExceptions.invalidInput("Room id must not be null");
        }
        // Gọi repository để lấy dữ liệu (có pagination)
        List<Seat> seats = seatRepository.findByRoomId(roomId);

        // Convert Domain -> DTO Response
        return seats.stream()
                .map(mapper::toResponse)
                .toList();
    }
}
